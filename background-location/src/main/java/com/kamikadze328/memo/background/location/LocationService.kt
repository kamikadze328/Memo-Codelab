package com.kamikadze328.memo.background.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.kamikadze328.memo.core.android.permissions.isFineLocationGranted
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {
    companion object {
        private val isRunning = AtomicBoolean(false)
        internal fun startService(context: Context) {
            if (isRunning.get()) return

            ContextCompat.startForegroundService(
                context, Intent(context, LocationService::class.java)
            )
        }

        internal fun stopService(context: Context) {
            if (!isRunning.get()) return

            context.stopService(Intent(context, LocationService::class.java))
        }
    }

    @Inject
    internal lateinit var presenter: LocationPresenter

    override fun onCreate() {
        super.onCreate()
        isRunning.set(true)
        presenter.onServiceCreate(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isFineLocationGranted()) {
            presenter.requestLocationUpdates()

            val notification = presenter.createLocationServiceNotification()
            startForeground(
                LocationNotificationHelper.NOTIFICATION_LOCATION_SERVICE_ID,
                notification
            )
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }


    override fun onDestroy() {
        presenter.onDestroy()

        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
            NotificationManagerCompat.from(this)
                .cancel(LocationNotificationHelper.NOTIFICATION_LOCATION_SERVICE_ID)
        } catch (_: Throwable) {
        }

        super.onDestroy()

        isRunning.set(false)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}