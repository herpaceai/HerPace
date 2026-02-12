package com.herpace

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HerPaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Disable Crashlytics collection in debug builds
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        // Set global uncaught exception handler to record to Crashlytics
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("HerPace", "Uncaught exception", throwable)
            FirebaseCrashlytics.getInstance().recordException(throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
