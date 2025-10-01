package com.kamikadze328.memo.background.location

import android.Manifest
import android.app.Notification
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kamikadze328.memo.core.android.coroutines.IDispatcherProvider
import com.kamikadze328.memo.domain.model.Memo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LocationPresenter @Inject constructor(
    private val notificationHelper: LocationNotificationHelper,
    private val locationUpdateProcessor: LocationUpdateProcessor,
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
        notificationHelper.showMemoLocatedNotification(memo)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION])
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