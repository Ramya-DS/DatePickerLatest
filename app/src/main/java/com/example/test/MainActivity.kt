package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), FragmentToActivityInterface {

    lateinit var activityButton: Button
    lateinit var siblingButton:Button
    lateinit var childButton:Button

    lateinit var dateText:TextView
    lateinit var timeText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityButton=findViewById(R.id.activity)
        siblingButton=findViewById(R.id.sibling_fragment)
        childButton=findViewById(R.id.child_fragment)

        dateText=findViewById(R.id.date)
        timeText=findViewById(R.id.time)

        activityButton.setOnClickListener {
            DateTimeFragment().show(supportFragmentManager,"PICKER")
        }

        siblingButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container,PickFragment.newInstance(true),"PICK").addToBackStack(null).commit()
        }
        childButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container,PickFragment.newInstance(false),"PICK").addToBackStack(null).commit()
        }

    }

    override fun dialogcloseActivity() {
        supportFragmentManager.let {
            it.beginTransaction().remove(it.findFragmentByTag("PICKER")!!) .commit()
        }
    }

    override fun dataChangedActivity(isDate: Boolean, data: String) {
        if(isDate)
            dateText.text=data
        else
            timeText.text=data
    }
}
