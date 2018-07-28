package com.refundpal.www.refundpal.injection

import com.google.gson.Gson

import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.util.RefundService
import com.refundpal.www.refundpal.util.PrefManager

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import com.refundpal.www.refundpal.util.UserSessionManager


@Module
class RefundModule(private val mApplication: RefundApplication) {

    @Provides
    @Singleton
    internal fun providesApplication(): RefundApplication {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    internal fun providesUserSessionManager(prefManager: PrefManager, gson: Gson): UserSessionManager {
        return UserSessionManager(prefManager, gson)
    }

    @Provides
    @Singleton
    internal fun providesRefundService(prefManager: PrefManager, refundApplication: RefundApplication): RefundService {
        return RefundService(prefManager, refundApplication)
    }

    @Provides
    @Singleton
    internal fun providesPrefManager(app: RefundApplication, gson: Gson): PrefManager {
        return PrefManager(app, gson)
    }

}
