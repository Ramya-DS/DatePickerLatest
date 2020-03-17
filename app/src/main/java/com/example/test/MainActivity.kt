package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), Listener {

    lateinit var activityButton: Button
    lateinit var siblingButton: Button
    lateinit var childButton: Button

    lateinit var dateText: TextView
    lateinit var timeText: TextView

    var mChildFragmentRemover: ChildFragmentRemover?=null
    var isSibling: Boolean?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            mChildFragmentRemover=supportFragmentManager.findFragmentByTag("PICK") as PickFragment
        }

        activityButton = findViewById(R.id.activity)
        siblingButton = findViewById(R.id.sibling_fragment)
        childButton = findViewById(R.id.child_fragment)

        dateText = findViewById(R.id.date)
        timeText = findViewById(R.id.time)

        activityButton.setOnClickListener {
            DateTimeFragment().show(supportFragmentManager, "PICKER")
        }

        siblingButton.setOnClickListener {
            isSibling=true
            val fragment=PickFragment.newInstance(isSibling!!)
            mChildFragmentRemover=fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,fragment , "PICK")
                .addToBackStack(null).commit()
        }
        childButton.setOnClickListener {
            isSibling=false
            val fragment=PickFragment.newInstance(isSibling!!)
            mChildFragmentRemover=fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "PICK")
                .addToBackStack(null).commit()
        }

    }

    override fun closeDialog() {
        if(isSibling==null || isSibling!!){
            supportFragmentManager.let {
                it.beginTransaction().remove(it.findFragmentByTag("PICKER")!!).commit()
            }
        }
       else{
            mChildFragmentRemover!!.removeChildFragment()
        }

    }

    override fun dataChanged(isDate: Boolean, data: String) {
        if (isDate)
            dateText.text = data
        else
            timeText.text = data
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        isSibling?.let{
            outState.putBoolean("isSibling",it)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            isSibling=it.getBoolean("isSibling")
        }
    }
}
