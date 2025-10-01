package com.kamikadze328.memo

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.kamikadze328.memo.repository.Repository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A processor responsible for observing opened memos and starting the [LocationService]
 * when any of them have an associated location. This ensures that the location service is
 * running only when it is actually needed.
 */
object LocationServiceStartingProcessor {
    suspend fun observeAndManageLocationService(context: Context) {
        Repository.collectOpened()
            .map { memos -> memos.any { it.location != null } }
            .distinctUntilChanged()
            .collect { hasMemosWithLocation ->
                if (hasMemosWithLocation) {
                    startService(context.applicationContext)
                } else {
                    stopService(context.applicationContext)
                }
            }
    }

    private fun startService(context: Context) {
        if (LocationService.isRunning.get()) return

        ContextCompat.startForegroundService(
            context, Intent(context, LocationService::class.java)
        )
    }

    private fun stopService(context: Context) {
        if (!LocationService.isRunning.get()) return
        
        context.stopService(Intent(context, LocationService::class.java))
    }
}