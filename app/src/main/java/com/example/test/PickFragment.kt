package com.example.test


import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class PickFragment : Fragment(), ChildFragmentRemover {

    private var isSibling: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pick, container, false)
        val pickButton = rootView.findViewById<Button>(R.id.pick)

        pickButton.setOnClickListener {
            isSibling?.let {
                if (it)
                    DateTimeFragment.newInstance(it).show(
                        activity!!.supportFragmentManager,
                        "PICKER"
                    )
                else
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

    override fun removeChildFragment() {
        if (!(isSibling!!)) {
            childFragmentManager.beginTransaction()
                .remove(childFragmentManager.findFragmentByTag("PICKER")!!).commit()
        } else {
            activity!!.supportFragmentManager.let { manager ->
                manager.findFragmentByTag("PICKER")?.let { fragment ->
                    manager.beginTransaction().remove(fragment).commit()
                }
            }
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
}