package com.kamikadze328.memo.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

/**
 * Represents a memo location.
 *
 * @param latitude The latitude of the location.
 * @param longitude The longitude of the location.
 */
@Parcelize
data class MemoLocation(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
) : Parcelable