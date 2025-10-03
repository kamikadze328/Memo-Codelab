package com.kamikadze328.memo.feature.choose.location

import android.os.Parcelable
import com.kamikadze328.memo.domain.model.MemoLocation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseLocationResult(
    val location: MemoLocation?,
) : Parcelable