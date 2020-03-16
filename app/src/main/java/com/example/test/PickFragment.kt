package com.example.test


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * A simple [Fragment] subclass.
 */
class PickFragment : Fragment() {

    private var isSibling: Boolean= true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pick, container, false)
        val pickButton = rootView.findViewById<Button>(R.id.pick)

        pickButton.setOnClickListener {
            if (isSibling)
                DateTimeFragment.newInstance(true).show(activity!!.supportFragmentManager, "PICKER")
            else
                DateTimeFragment.newInstance(false).show(childFragmentManager, "PICKER")
        }
        return rootView
    }

   companion object{
       fun newInstance(flag: Boolean): PickFragment{
           val fragment = PickFragment()
           val bundle = Bundle()
           bundle.putBoolean("isSibling", flag)
           fragment.arguments =bundle

           return fragment
       }
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isSibling=it.getBoolean("isSibling")
        }
    }
}
