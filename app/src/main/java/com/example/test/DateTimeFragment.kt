package com.example.test


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_date_time.*
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class DateTimeFragment : DialogFragment(), DateFragment.Listener, TimeFragment.Listener {

    var isSibling: Boolean? = null
    var listener: Listener? = null


    fun setListenerCallback(callback: Listener) {
        listener = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_date_time, container, false)

        val dateTimeViewPager = rootView.findViewById<ViewPager2>(R.id.date_time_viewpager)
        dateTimeViewPager.adapter = PagerAdapter(childFragmentManager, lifecycle)

        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, dateTimeViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "DATE"
                1 -> "TIME"
                else -> ""
            }
        }.attach()

        return rootView
    }

    inner class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        androidx.viewpager2.adapter.FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    if (isSibling != null)
                        DateFragment.newInstance(isSibling!!)
                    else
                        DateFragment()
                }
                1 -> {
                    if (isSibling != null)
                        TimeFragment.newInstance(isSibling!!)
                    else
                        TimeFragment()
                }
                else -> DateFragment()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isSibling = it.getBoolean("isSibling")
        }
    }

    companion object {
        fun newInstance(flag: Boolean): DateTimeFragment {
            val fragment = DateTimeFragment()
            val bundle = Bundle()
            bundle.putBoolean("isSibling", flag)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isSibling?.let { outState.putBoolean("isSibling", it) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            isSibling = it.getBoolean("isSibling", true)
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is DateFragment)
            childFragment.setListenerCallback(this)
        if (childFragment is TimeFragment)
            childFragment.setListenerCallback(this)
    }

    override fun closeDialog() {
        this.dismiss()
    }

    override fun dataChanged(isDate: Boolean, data: String) {
        if (isSibling == null || !(isSibling!!))
            listener!!.dataChanged(isDate, data)
        else {
            val intent = Intent()
            intent.putExtra("isDate", isDate)
            intent.putExtra("data", data)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
    }

    interface Listener {
        fun dataChanged(isDate: Boolean, data: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isSibling == null) {
            listener = context as Listener
        }
    }
}