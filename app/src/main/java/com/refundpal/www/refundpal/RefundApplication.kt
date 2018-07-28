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

        val CAFE_SEARCH_STRING = "cafe"

        val LAST_LOCATION_LOC = "last_location"

        val CAFE_DATA = "cafe_data"
        val CONVERSATION_DATA = "conv_data"

        val MY_PERMISSIONS_ACCESS_FINE_LOCATION = 100
        var app: RefundApplication? = null

        val injectionComponent: InjectionComponent
            get() = app!!.mInjectionComponent!!
    }
}
