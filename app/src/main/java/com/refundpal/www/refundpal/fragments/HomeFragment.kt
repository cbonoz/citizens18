package com.refundpal.www.refundpal.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.util.UserSessionManager
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseMainFragment() {

   @Inject
   lateinit var userSessionManager: UserSessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        RefundApplication.injectionComponent.inject(this)
        return v
    }

    // View is non-null in onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setupText: String
        val isDone: String? = userSessionManager.getUser().attributes.get("done")
        if (isDone != null) {
            setupText = getString(R.string.review_my_saving_profile)
        } else {
            setupText = getString(R.string.prepare_for_my_refund)
        }

        questionButton.text = setupText
        questionButton.setOnClickListener {
            Toast.makeText(activity, "Let's start your Refund Journey!", Toast.LENGTH_SHORT).show()
            loadQuestionFragment(1, setupText)
        }



        reportButton.setOnClickListener {
            loadFragmentIntoMain(ReportFragment(), getString(R.string.view_tax_saving_report))
        }

        profileButton.setOnClickListener {
            loadFragmentIntoMain(ProfileFragment(), getString(R.string.view_my_profile))
        }
    }


}
