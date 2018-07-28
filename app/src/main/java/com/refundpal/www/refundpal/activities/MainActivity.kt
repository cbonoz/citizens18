package com.refundpal.www.refundpal.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private var lastFragmentSelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment(), getString(R.string.app_name))
    }

    fun replaceFragment(fragment: Fragment, fragmentTitle: String) {
        if (fragmentTitle == lastFragmentSelected) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        title = fragmentTitle
        transaction.addToBackStack(null).commit()
        lastFragmentSelected = fragmentTitle
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
