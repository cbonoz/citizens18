package com.refundpal.www.refundpal.fragments

import android.support.v4.app.Fragment
import com.refundpal.www.refundpal.activities.MainActivity

abstract class BaseMainFragment : Fragment() {

    fun loadMainFragment(fragment: Fragment, title: String) {
        (activity as MainActivity).replaceFragment(fragment, title)
    }

}
