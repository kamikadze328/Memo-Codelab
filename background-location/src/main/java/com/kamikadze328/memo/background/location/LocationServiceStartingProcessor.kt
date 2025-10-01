package com.kamikadze328.memo.background.location

import android.content.Context
import com.kamikadze328.memo.domain.repository.IMemoRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A processor responsible for observing opened memos and starting the [LocationService]
 * when any of them have an associated location. This ensures that the location service is
 * running only when it is actually needed.
 */
class LocationServiceStartingProcessor @Inject constructor(
    private val memoRepository: IMemoRepository,
) {
    suspend fun observeAndManageService(context: Context) {
        memoRepository.collectOpened()
            .map { memos -> memos.any { it.reminderLocation != null } }
            .distinctUntilChanged()
            .collect { hasMemosWithLocation ->
                if (hasMemosWithLocation) {
                    LocationService.startService(context.applicationContext)
                } else {
                    LocationService.stopService(context.applicationContext)
                }
            }
    }


}