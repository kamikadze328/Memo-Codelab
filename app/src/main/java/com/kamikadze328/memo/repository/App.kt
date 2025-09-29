package com.kamikadze328.memo.repository

import android.app.Application

/**
 * Extension of the Android Application class.
 */
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}