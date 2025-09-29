package com.kamikadze328.memo.background

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kamikadze328.memo.core.android.coroutines.ScopeProvider
import com.kamikadze328.memo.core.android.permissions.isCoarseLocationGranted
import com.kamikadze328.memo.core.android.permissions.isFineLocationGranted
import com.kamikadze328.memo.core.android.permissions.isPostNotificationsGranted
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.repository.Repository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class LocationService : Service() {

    companion object {
        private const val TAG = "LocationService"
        private const val LOCATION_REQUEST_INTERVAL_MS = 10_000L
        private const val LOCATION_REQUEST_DEBOUNCE_MS = 5_000L
        private const val LOCATION_REQUEST_MIN_UPDATES_METERS = 10.0f
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val notificationHelper by lazy { LocationNotificationHelper(applicationContext) }
    private val coroutineScope = ScopeProvider.supervisorScope()
    private val locationProcessor: LocationUpdateProcessor by lazy {
        LocationUpdateProcessor(
            repository = Repository,
        )
    }
    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation = p0.lastLocation ?: return

                try {
                    coroutineScope.launch {
                        locationProcessor.onLocationUpdate(
                            lastUserLocation = lastLocation,
                            notifyLocationMemoNearby = ::notifyLocationMemoNearby
                        )
                    }
                } catch (e: Throwable) {
                    coroutineScope.ensureActive()
                    Log.e(TAG, "Failed to process location update", e)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isFineLocationGranted() && isCoarseLocationGranted()) {
            requestLocationUpdates()

            val notification = notificationHelper.createLocationServiceNotification()
            startForeground(
                LocationNotificationHelper.NOTIFICATION_LOCATION_SERVICE_ID,
                notification
            )
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL_MS)
            .setMinUpdateIntervalMillis(LOCATION_REQUEST_DEBOUNCE_MS)
            .setMinUpdateDistanceMeters(LOCATION_REQUEST_MIN_UPDATES_METERS)
            .setWaitForAccurateLocation(true)
            .build()

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onDestroy() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
        coroutineScope.cancel()

        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
            NotificationManagerCompat.from(this)
                .cancel(LocationNotificationHelper.NOTIFICATION_LOCATION_SERVICE_ID)
        } catch (_: Throwable) {
        }

        super.onDestroy()

    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun notifyLocationMemoNearby(memo: Memo) {
        val notification = notificationHelper.createMemoLocatedNotification(memo)

        showNotification(memo.id, notification)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(id: Long, notification: Notification) {
        if (!isPostNotificationsGranted()
            && !NotificationManagerCompat.from(this).areNotificationsEnabled()
        ) {
            return
        }

        NotificationManagerCompat.from(this).notify(id.toInt(), notification)
    }
}