package com.kamikadze328.memo.repository

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import com.kamikadze328.memo.LocationServiceStartingProcessor
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration

/**
 * Extension of the Android Application class.
 */
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
        initOsmDroid()
        initLocationService()
    }

    private fun initLocationService() {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            ProcessLifecycleOwner.get().repeatOnLifecycle(Lifecycle.State.STARTED) {
                LocationServiceStartingProcessor.observeAndManageLocationService(applicationContext)
            }
        }
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