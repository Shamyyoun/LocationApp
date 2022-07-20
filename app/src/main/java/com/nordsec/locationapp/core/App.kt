package com.nordsec.locationapp.core

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
    }

    companion object {
        private var currentApplication: Application? = null
        private val application: Application?
            get() = currentApplication
        val context: Context
            get() = application!!.applicationContext
    }
}