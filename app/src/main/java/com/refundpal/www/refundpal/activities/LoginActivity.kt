package com.refundpal.www.refundpal.activities


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.refundpal.www.refundpal.R
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.TextView
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.models.User
import com.refundpal.www.refundpal.models.transaction.Token
import com.refundpal.www.refundpal.util.RefundService
import com.refundpal.www.refundpal.util.UserSessionManager
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginActivity : Activity() {

    @Inject
    lateinit var refundService: RefundService
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RefundApplication.injectionComponent.inject(this)
        setContentView(R.layout.activity_login)

        input_email.setText(resources.getString(R.string.sample_username), TextView.BufferType.EDITABLE)
        input_password.setText(resources.getString(R.string.sample_password), TextView.BufferType.EDITABLE)

        loginButton.setOnClickListener {
            val username = input_email.getText().toString()
            val password = input_password.getText().toString()
            val consumerKey = getString(R.string.citizen_key)

            val progressDialog = ProgressDialog(this, R.style.AppTheme)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Authenticating...")
            progressDialog.show()

            val directAuthString = "DirectLogin username=\"${username}\",   password=\"${password}\",  consumer_key=\"${consumerKey}\""
            val url = refundService.postAuthUrl()
            url.httpPost().header(Pair("Authorization", directAuthString))
                    .response { request, response, result ->
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                Timber.e(ex, "error authenticating $url")
                                progressDialog.dismiss()
                            }
                            is Result.Success -> {
                                val data = result.get()
                                Timber.d("data ${data}")
                                val s = data.toString(Charset.defaultCharset())
                                val token = gson.fromJson(s, Token::class.java)
                                val user = User(username)
                                user.token = token.token
                                userSessionManager.saveUser(user)
                                progressDialog.dismiss()
                                proceed()
                            }
                        }
                    }
        }
    }

    private fun proceed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
