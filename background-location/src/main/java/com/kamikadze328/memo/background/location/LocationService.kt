package com.kamikadze328.memo.background.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.kamikadze328.memo.core.android.permissions.isFineLocationGranted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    internal lateinit var presenter: LocationPresenter

    override fun onCreate() {
        super.onCreate()
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

    }

    override fun onBind(intent: Intent?): IBinder? = null
}