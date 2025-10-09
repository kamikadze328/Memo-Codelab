package com.kamikadze328.memo.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class MemoLocation(
    val latitude: Double,
    val longitude: Double,
)