package com.kamikadze328.memo

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.memo.background.location.LocationServiceStartingProcessor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Extension of the Android Application class.
 */
@HiltAndroidApp
internal class App : Application() {
    @Inject
    lateinit var locationServiceStartingProcessor: LocationServiceStartingProcessor

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            ProcessLifecycleOwner.get().repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationServiceStartingProcessor.observeAndManageService(applicationContext)
            }
        }
    }
}