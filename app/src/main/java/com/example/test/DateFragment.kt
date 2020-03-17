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
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DateFragment : Fragment(){

    companion object {
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        fun newInstance(flag: Boolean): DateFragment {
            val fragment = DateFragment()
            val bundle = Bundle()
            bundle.putBoolean("isSibling", flag)
            fragment.arguments = bundle

            return fragment
        }
    }

    private var listener: Listener? = null

    private lateinit var datePicker: DatePicker
    private var isSibling: Boolean? = null

    private var currentDate: Int = 0
    private var currentMonth: Int = 0
    private var currentYear: Int = 0


//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_date, container, false)

        datePicker = rootView.findViewById(R.id.date_picker)

        val cancelButton: Button = rootView.findViewById(R.id.cancelButtonDate)
        val clearButton: Button = rootView.findViewById(R.id.clearButtonDate)
        val doneButton: Button = rootView.findViewById(R.id.doneButtonDate)

        val initialDate: Int = datePicker.dayOfMonth
        val initialMonth: Int = datePicker.month
        val initialYear: Int = datePicker.year

        currentDate = initialDate
        currentMonth = initialMonth
        currentYear = initialYear

//        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
//            currentDate = dayOfMonth
//            currentMonth = monthOfYear
//            currentYear = year
//        }

        datePicker.init(currentYear,currentMonth,currentDate) { view, year, monthOfYear, dayOfMonth ->
            currentDate = dayOfMonth
            currentMonth = monthOfYear
            currentYear = year
        }

    cancelButton.setOnClickListener {
            listener!!.closeDialog()
        }

        doneButton.setOnClickListener {
            listener!!.dataChanged(
                true,
                formatter.format(Date(currentYear - 1900, currentMonth, currentDate))
            )
            listener!!.closeDialog()
        }
        clearButton.setOnClickListener {
            currentDate = initialDate
            currentMonth = initialMonth
            currentYear = initialYear
            datePicker.updateDate(initialYear, initialMonth, initialDate)
        }

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("year", currentYear)
        outState.putInt("month", currentMonth)
        outState.putInt("date", currentDate)
        isSibling?.let { outState.putBoolean("isSibling", it) }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            isSibling = it.getBoolean("isSibling")
            datePicker.updateDate(it.getInt("year"), it.getInt("month"), it.getInt("date"))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.let {
            isSibling = it.getBoolean("isSibling")
        }

        listener=context as Listener

        if (listener == null) {
            throw ClassCastException("$context implement")
        }
    }
//    override fun onDetach() {
//        super.onDetach()
//        listener=null
//    }

}