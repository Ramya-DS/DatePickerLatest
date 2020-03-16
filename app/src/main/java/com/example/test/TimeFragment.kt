package com.example.test


import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import java.lang.ClassCastException
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class TimeFragment : Fragment() {

    companion object {
        fun newInstance(flag:Boolean): TimeFragment{
            val fragment = TimeFragment()
            val bundle = Bundle()
            bundle.putBoolean("isSibling", flag)
            fragment.arguments =bundle

            return fragment
        }
    }

    private var mFragmentToActivityInterface: FragmentToActivityInterface? = null
    private var mFragmentToFragmentInterface: FragmentToFragmentInterface? = null

    private lateinit var timePicker: TimePicker
    private var isSibling: Boolean?=null

    private var currentHour = 0
    private var currentMinute = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_time, container, false)

        timePicker = rootView.findViewById(R.id.time_picker)

        val cancelButton: Button = rootView.findViewById(R.id.cancelButtonTime)
        val clearButton: Button = rootView.findViewById(R.id.clearButtonTime)
        val doneButton: Button = rootView.findViewById(R.id.doneButtonTime)

        val initialHour = timePicker.hour
        val initialMinute = timePicker.minute

        currentHour = initialHour
        currentMinute = initialMinute


        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            currentHour = hourOfDay
            currentMinute = minute
        }

        cancelButton.setOnClickListener {
            if (isSibling == null)
                mFragmentToActivityInterface!!.dialogcloseActivity()
            else {
                mFragmentToFragmentInterface!!.dialogCloseFragment()
            }
        }

        clearButton.setOnClickListener {
            currentMinute = initialMinute
            currentHour = initialHour
            timePicker.hour = initialHour
            timePicker.minute = initialMinute
        }

        doneButton.setOnClickListener {
            val time: String =
                when {
                    currentHour > 12 -> "${currentHour - 12} : $currentMinute PM"
                    currentHour == 12 -> {
                        "12 : $currentMinute PM"
                    }
                    else -> "$currentHour : $currentMinute AM"
                }
            if (isSibling == null) {
                mFragmentToActivityInterface!!.dataChangedActivity(false, time)
                mFragmentToActivityInterface!!.dialogcloseActivity()
            } else {
                mFragmentToFragmentInterface!!.dataChangedFragment(false,time)
                mFragmentToFragmentInterface!!.dialogCloseFragment()
            }
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("hour", currentHour)
        outState.putInt("minute", currentMinute)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            timePicker.hour = it.getInt("hour")
            timePicker.minute = it.getInt("minute")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.let{
            isSibling=it.getBoolean("isSibling")
        }

        if (isSibling == null) mFragmentToActivityInterface = context as FragmentToActivityInterface
        else mFragmentToFragmentInterface = context as FragmentToFragmentInterface

        if (mFragmentToActivityInterface == null && mFragmentToFragmentInterface == null) {
            throw ClassCastException("$context implement")
        }
    }
}
