package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE

class MainActivity : AppCompatActivity(), DateTimeFragment.Listener, PickFragment.Listener {

    lateinit var activityButton: Button
    lateinit var siblingButton: Button
    lateinit var childButton: Button

    lateinit var dateText: TextView
    lateinit var timeText: TextView

    var isSibling: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityButton = findViewById(R.id.activity)
        siblingButton = findViewById(R.id.sibling_fragment)
        childButton = findViewById(R.id.child_fragment)

        dateText = findViewById(R.id.date)
        timeText = findViewById(R.id.time)

        activityButton.setOnClickListener {
            supportFragmentManager.popBackStack("PICK", POP_BACK_STACK_INCLUSIVE)
            DateTimeFragment().show(supportFragmentManager, "PICKER")
        }

        siblingButton.setOnClickListener {
            isSibling = true
            val fragment = PickFragment.newInstance(isSibling!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "PICK")
                .addToBackStack(null).commit()
        }
        childButton.setOnClickListener {
            isSibling = false
            val fragment = PickFragment.newInstance(isSibling!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "PICK")
                .addToBackStack(null).commit()
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
        isSibling = savedInstanceState.getBoolean("isSibling", true)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if(fragment is DateTimeFragment)
            fragment.setListenerCallback(this)
        if(fragment is PickFragment)
            fragment.setListenerCallback(this)
    }

}