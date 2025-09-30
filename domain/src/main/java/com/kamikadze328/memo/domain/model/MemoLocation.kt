package com.kamikadze328.memo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoLocation(
    val latitude: Double,
    val longitude: Double,
) : Parcelable