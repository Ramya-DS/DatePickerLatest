package com.example.test


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class PickFragment : Fragment(), DateTimeFragment.Listener {

    private var isSibling: Boolean? = null
    var listener:Listener?=null

    fun setListenerCallback(callback: Listener) {
        listener = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pick, container, false)
        val pickButton = rootView.findViewById<Button>(R.id.pick)

        pickButton.setOnClickListener {
            isSibling?.let {
                if (it) {
                    val frag = DateTimeFragment.newInstance(it)
                    frag.setTargetFragment(this, 1)
                    frag.show(
                        activity!!.supportFragmentManager,
                        "PICKER"
                    )
                } else
                    DateTimeFragment.newInstance(it).show(childFragmentManager, "PICKER")
            }
        }
        return rootView
    }

    companion object {
        fun newInstance(flag: Boolean): PickFragment {
            val fragment = PickFragment()
            val bundle = Bundle()
            bundle.putBoolean("isSibling", flag)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isSibling = it.getBoolean("isSibling", true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isSibling?.let {
            outState.putBoolean("isSibling", it)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            isSibling = it.getBoolean("isSibling", true)
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is DateTimeFragment)
            childFragment.setListenerCallback(this)
    }

    override fun dataChanged(isDate: Boolean, data: String) {
        listener?.dataChanged(isDate,data)
    }

    interface Listener {
        fun dataChanged(isDate: Boolean, data: String)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                data?.let{
                    listener?.dataChanged(it.getBooleanExtra("isDate",true), it.getStringExtra("data")!!)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener=context as Listener
    }
}