package com.refundpal.www.refundpal.activities

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.fragments.HomeFragment
import com.refundpal.www.refundpal.models.User
import com.refundpal.www.refundpal.models.transaction.TransactionResponse
import com.refundpal.www.refundpal.util.RefundService
import com.refundpal.www.refundpal.util.UserSessionManager
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.SEND_SMS
import android.support.v4.app.ActivityCompat
import android.Manifest.permission.READ_CONTACTS
import android.support.v4.content.ContextCompat
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var refundService: RefundService
    @Inject
    lateinit var userSessionManager: UserSessionManager
    @Inject
    lateinit var gson: Gson

    private var lastFragmentSelected = ""
    private lateinit var user: User

    private var transactionResponse: TransactionResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        RefundApplication.injectionComponent.inject(this)
        user = userSessionManager.getUser()
        replaceFragment(HomeFragment(), getString(R.string.app_name), false, false)
        requestSmsPermission()
        fetchAccountInfo()
    }

    fun getTransactionResponse(): TransactionResponse? {
        if (transactionResponse == null) {
            fetchAccountInfo()
        }
        return transactionResponse
    }

    private fun fetchAccountInfo() {
        val url = refundService.getTransactionsUrl(getString(R.string.bank_id), getString(R.string.account_id))
        url.httpGet().header(Pair("Authorization", "DirectLogin token=${user.token}"))
                .response { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Timber.e(ex, "error getting tx data: $url")
                        }
                        is Result.Success -> {
                            val data = result.get()
                            val s = data.toString(Charset.defaultCharset())
                            Timber.d("tx data ${s}")
                            transactionResponse = gson.fromJson(s, TransactionResponse::class.java)
                        }
                    }
                }
    }


    fun replaceFragment(fragment: Fragment, fragmentTitle: String, backstack: Boolean, animate: Boolean) {
//        if (fragmentTitle == lastFragmentSelected) {
//            return
//        }
        title = fragmentTitle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        if (animate) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }

        transaction.commit()
        lastFragmentSelected = fragmentTitle
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private val PERMISSION_SEND_SMS = 123

    private fun requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSION_SEND_SMS)
        } else {
            // permission already granted run sms send
            Timber.d("SMS Permission Granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            else -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Timber.d("SMS Permission Granted")
                } else {
                    // permission denied
                    Toast.makeText(this, "SMS permission is required", Toast.LENGTH_SHORT).show()
                    requestSmsPermission()
                }
                return
            }
        }
    }

}
