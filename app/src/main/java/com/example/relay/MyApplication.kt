package com.example.relay

import android.app.Application

class MyApplication: Application(){
    companion object {
        lateinit var prefs: PrefsManager
    }

    override fun onCreate() {
        prefs = PrefsManager(applicationContext)
        super.onCreate()
    }
}