package com.kamikadze328.memo.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MemoLocation(
    val latitude: Double,
    val longitude: Double,
) : Parcelable