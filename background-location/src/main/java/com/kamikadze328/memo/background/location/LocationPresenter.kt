package com.kamikadze328.memo.background.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
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
import com.kamikadze328.memo.core.android.coroutines.IDispatcherProvider
import com.kamikadze328.memo.core.android.permissions.isPostNotificationsGranted
import com.kamikadze328.memo.domain.model.Memo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LocationPresenter @Inject constructor(
    private val notificationHelper: LocationNotificationHelper,
    private val locationUpdateProcessor: LocationUpdateProcessor,
    @param:ApplicationContext
    private val applicationContext: Context,
    dispatcherProvider: IDispatcherProvider,
) {
    companion object {
        private const val TAG = "LocationService"
        private const val LOCATION_REQUEST_INTERVAL_MS = 10_000L
        private const val LOCATION_REQUEST_DEBOUNCE_MS = 5_000L
        private const val LOCATION_REQUEST_MIN_UPDATES_METERS = 10.0f
    }

    private val coroutineScope = CoroutineScope(dispatcherProvider.main + SupervisorJob())

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) = onLocationUpdate(p0)
        }
    }
    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun onServiceCreate(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private fun onLocationUpdate(result: LocationResult) {
        val lastLocation = result.lastLocation ?: return
        coroutineScope.launch {
            try {
                locationUpdateProcessor.onLocationUpdate(
                    lastUserLocation = lastLocation,
                    notifyLocationMemoNearby = ::notifyLocationMemoNearby
                )
            } catch (e: Throwable) {
                coroutineScope.ensureActive()
                Log.e(TAG, "Failed to process location update", e)
            }
        }
    }

    private fun notifyLocationMemoNearby(memo: Memo) {
        val notification = notificationHelper.createMemoLocatedNotification(memo)

        showNotification(memo.id, notification)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(id: Long, notification: Notification) {
        if (!applicationContext.isPostNotificationsGranted()
            && !NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()
        ) {
            return
        }

        NotificationManagerCompat.from(applicationContext).notify(id.toInt(), notification)
    }

    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    fun requestLocationUpdates() {
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

    fun createLocationServiceNotification(): Notification {
        return notificationHelper.createLocationServiceNotification()
    }

    fun onDestroy() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
        coroutineScope.cancel()
    }
}