package com.kamikadze328.memo.feature.choose.location

import androidx.compose.runtime.Immutable
import com.kamikadze328.memo.domain.model.MemoLocation

@Immutable
data class ChooseLocationUiState(
    val location: MemoLocation,
    val initialLocation: MemoLocation?,
    val canChoose: Boolean,
) {
    companion object {
        val BATUMI_LOCATION = MemoLocation(41.6413, 41.6359)
        val DEFAULT_LOCATION = BATUMI_LOCATION
        val EMPTY = ChooseLocationUiState(
            location = DEFAULT_LOCATION,
            initialLocation = null,
            canChoose = false,
        )

    }
}