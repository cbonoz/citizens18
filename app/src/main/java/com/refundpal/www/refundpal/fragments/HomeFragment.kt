package com.refundpal.www.refundpal.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.refundpal.www.refundpal.R
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        return v
    }

    // View is non-null in onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionButton.setOnClickListener {
            Toast.makeText(activity, "Let's start your Refund Journey!", Toast.LENGTH_SHORT).show()
            loadMainFragment(QuestionFragment(), getString(R.string.prepare_for_my_refund))
        }

        reportButton.setOnClickListener {
            loadMainFragment(ReportFragment(), getString(R.string.view_tax_saving_report))
        }

        profileButton.setOnClickListener {
            loadMainFragment(ProfileFragment(), getString(R.string.view_my_profile))
        }
    }


}
