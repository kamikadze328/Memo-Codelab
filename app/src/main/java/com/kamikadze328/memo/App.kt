package com.kamikadze328.memo

import android.app.Application
import androidx.preference.PreferenceManager
import com.kamikadze328.memo.domain.repository.Repository
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