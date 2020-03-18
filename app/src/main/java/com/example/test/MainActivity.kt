package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE

class MainActivity : AppCompatActivity(), Listener {

    lateinit var activityButton: Button
    lateinit var siblingButton: Button
    lateinit var childButton: Button

    lateinit var dateText: TextView
    lateinit var timeText: TextView

    var mChildFragmentRemover: ChildFragmentRemover? = null
    var isSibling: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            supportFragmentManager.findFragmentByTag("PICK")?.let {
                isSibling = null
                mChildFragmentRemover = it as PickFragment
            }

        }

        activityButton = findViewById(R.id.activity)
        siblingButton = findViewById(R.id.sibling_fragment)
        childButton = findViewById(R.id.child_fragment)

        dateText = findViewById(R.id.date)
        timeText = findViewById(R.id.time)

        activityButton.setOnClickListener {
            supportFragmentManager.popBackStack("PICK",POP_BACK_STACK_INCLUSIVE)
            DateTimeFragment().show(supportFragmentManager, "PICKER")
//            supportFragmentManager.beginTransaction().add(DateTimeFragment(),"PICKER").addToBackStack(null).commit()
        }

        siblingButton.setOnClickListener {
            isSibling = true
            val fragment = PickFragment.newInstance(isSibling!!)
            mChildFragmentRemover = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "PICK")
                .addToBackStack(null).commit()
        }
        childButton.setOnClickListener {
            isSibling = false
            val fragment = PickFragment.newInstance(isSibling!!)
            mChildFragmentRemover = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "PICK")
                .addToBackStack(null).commit()
        }

    }

    override fun closeDialog() {
        if (mChildFragmentRemover != null)
            mChildFragmentRemover!!.removeChildFragment()
        else {
            supportFragmentManager.let {
                it.findFragmentByTag("PICKER")?.let { frag ->
                    it.beginTransaction().remove(frag).commit()
                }
            }
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
        isSibling?.let {
            outState.putBoolean("isSibling", it)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            isSibling = it.getBoolean("isSibling", true)
        }
    }
}