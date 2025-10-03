package com.kamikadze328.memo.feature.memo.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoDetailsArgs(
    val openingType: OpeningType,
) : Parcelable {
    @Parcelize
    sealed interface OpeningType : Parcelable {
        data object CreateNew : OpeningType
        data class View(val memoId: Long) : OpeningType
    }
}