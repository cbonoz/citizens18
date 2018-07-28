package com.refundpal.www.refundpal.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.activities.MainActivity
import com.refundpal.www.refundpal.models.transaction.TransactionResponse


abstract class BaseMainFragment : Fragment() {

    fun getTransactionResponse(): TransactionResponse? {
        return (activity as MainActivity).getTransactionResponse()
    }

    fun loadFragmentIntoMain(fragment: Fragment, title: String, backstack: Boolean = true, animate: Boolean = true) {
        (activity as MainActivity).replaceFragment(fragment, title, backstack, animate)
    }

    fun loadQuestionFragment(i: Int, title: String) {
        val args = Bundle()
        args.putInt(RefundApplication.QUESTION_INDEX, i)
        val f = QuestionFragment()
        f.arguments = args
        loadFragmentIntoMain(f, title, false)
    }

}
