package com.refundpal.www.refundpal.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson
import com.refundpal.www.refundpal.RefundApplication.Companion.app
import com.refundpal.www.refundpal.activities.SplashActivity
import com.refundpal.www.refundpal.models.User
import java.util.*

class UserSessionManager(private val prefManager: PrefManager, private val gson: Gson) {

    // TODO: replace with auth (deviceId ok for demo uniqueness for now, each device will be unique user).
    fun saveUser(user: User) {
        prefManager.saveJson(USER_ID_LOCATION, user)
    }

    fun hasLoggedInUser(): Boolean {
        val userString = prefManager.getString(USER_ID_LOCATION, "")
        return !userString.isNullOrBlank()
    }

    fun getUser(): User {
        val user = prefManager.getJson(USER_ID_LOCATION, User::class.java, User(""))
        if (user.name.isBlank()) {
            logout()
        }
        return user
    }

    private fun logout() {
        prefManager.clear(USER_ID_LOCATION)
        Toast.makeText(app, "Session Expired, please reregister", Toast.LENGTH_LONG).show()
        val intent = Intent(app, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app!!.startActivity(intent)
    }

    companion object {
        val USER_ID_LOCATION = "user_id"
    }

}