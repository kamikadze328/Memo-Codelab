package com.kamikadze328.memo.background.location

import android.location.Location
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.IMemoRepository
import javax.inject.Inject

internal class LocationUpdateProcessor @Inject constructor(
    private val repository: IMemoRepository,
) {
    companion object {
        private const val MAX_DISTANCE_TO_LOCATION_METERS = 200
    }

    suspend fun onLocationUpdate(
        lastUserLocation: Location,
        notifyLocationMemoNearby: (Memo) -> Unit
    ) {
        val openedMemos = repository.getOpen()
        openedMemos
            .filter { it.shouldNotifyMemoNearby(lastUserLocation) }
            .forEach { notifyLocationMemoNearby(it) }
    }

    private fun Memo.shouldNotifyMemoNearby(lastUserLocation: Location): Boolean {
        val memoLocation = reminderLocation ?: return false

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