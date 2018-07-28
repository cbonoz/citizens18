package com.refundpal.www.refundpal

import android.app.Application
import com.refundpal.www.refundpal.injection.DaggerInjectionComponent
import timber.log.Timber

import com.refundpal.www.refundpal.injection.RefundModule
import com.refundpal.www.refundpal.injection.InjectionComponent

class RefundApplication : Application() {
    private var mInjectionComponent: InjectionComponent? = null

    override fun onCreate() {
        super.onCreate()
        mInjectionComponent = DaggerInjectionComponent.builder()
                .refundModule(RefundModule(this))
                .build()
        Timber.plant(Timber.DebugTree());
        app = this
    }

    companion object {

        val USER_LOC = "user_location"

        val QUESTION_DATA = "question_data"
        val QUESTION_INDEX = "question_index"

        val MY_PERMISSIONS_ACCESS_FINE_LOCATION = 100
        var app: RefundApplication? = null

        val injectionComponent: InjectionComponent
            get() = app!!.mInjectionComponent!!
    }
}
