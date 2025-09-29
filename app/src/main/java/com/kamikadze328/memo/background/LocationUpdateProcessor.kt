package com.kamikadze328.memo.background

import android.location.Location
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocationUpdateProcessor(
    private val repository: Repository,
) {
    companion object {
        private const val MAX_DISTANCE_TO_LOCATION_METERS = 200
    }

    suspend fun onLocationUpdate(
        lastUserLocation: Location,
        notifyLocationMemoNearby: (Memo) -> Unit
    ) {
        val openedMemos = withContext(Dispatchers.IO) {
            repository.getOpen()
        }
        openedMemos
            .filter { it.shouldNotifyMemoNearby(lastUserLocation) }
            .forEach { notifyLocationMemoNearby(it) }
    }

    private fun Memo.shouldNotifyMemoNearby(lastUserLocation: Location): Boolean {
        val memoLocation = location ?: return false

        val userDistanceToMemo = lastUserLocation.distanceTo(memoLocation.toLocation())
        return userDistanceToMemo < MAX_DISTANCE_TO_LOCATION_METERS
    }

    private fun MemoLocation.toLocation(): Location {
        return Location("").apply {
            latitude = this@toLocation.latitude
            longitude = this@toLocation.longitude
        }

    }
}