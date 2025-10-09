package com.kamikadze328.memo

import android.location.Location
import com.kamikadze328.memo.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocationUpdateProcessor(
    private val repository: Repository,
    private val notificationHelper: LocationNotificationHelper,
) {
    companion object {
        private const val MAX_DISTANCE_TO_LOCATION_METERS = 200.0
    }

    suspend fun onLocationUpdate(lastUserLocation: Location) {
        val memos = withContext(Dispatchers.IO) {
            repository
                .findNearMemoByFlatDistance(
                    latitude = lastUserLocation.latitude,
                    longitude = lastUserLocation.longitude,
                    radiusMeters = MAX_DISTANCE_TO_LOCATION_METERS,
                )
        }

        memos.forEach {
            notificationHelper.showMemoLocatedNotification(it)
        }
    }
}