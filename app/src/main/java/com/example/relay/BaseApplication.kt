package com.example.relay

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}