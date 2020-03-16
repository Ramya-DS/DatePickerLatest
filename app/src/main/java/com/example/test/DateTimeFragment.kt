package com.example.test


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_date_time.*
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class DateTimeFragment : DialogFragment(), FragmentToFragmentInterface {

    var isSibling: Boolean? = null

    var mFragmentToActivityInterface: FragmentToActivityInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_date_time, container, false)

        val dateTimeViewPager = rootView.findViewById<ViewPager2>(R.id.date_time_viewpager)
        dateTimeViewPager.adapter = PagerAdapter(this.activity!!)

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

    inner class PagerAdapter(fragmentActivity: FragmentActivity) :
        androidx.viewpager2.adapter.FragmentStateAdapter(fragmentActivity) {

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

    override fun dialogCloseFragment() {
        this.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentToActivityInterface=context as FragmentToActivityInterface

        if(mFragmentToActivityInterface==null)
            throw ClassCastException("$context should implement")
    }

    override fun dataChangedFragment(isDate: Boolean, data: String) {
        mFragmentToActivityInterface!!.dataChangedActivity(isDate,data)
    }

}
