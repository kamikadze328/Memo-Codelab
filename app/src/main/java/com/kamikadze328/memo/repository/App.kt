package com.kamikadze328.memo.repository

import android.app.Application
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

/**
 * Extension of the Android Application class.
 */
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
        initOsmDroid()
    }

    private fun initOsmDroid() {
        with(Configuration.getInstance()) {
            load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            )
            userAgentValue = packageName
        }
    }
}