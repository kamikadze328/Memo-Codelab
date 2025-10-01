package com.kamikadze328.memo.feature.choose.location

import androidx.compose.runtime.Immutable
import com.kamikadze328.memo.domain.model.MemoLocation

@Immutable
data class ChooseLocationUiState(
    val location: MemoLocation,
    val initialLocation: MemoLocation?,
    val canChoose: Boolean,
)