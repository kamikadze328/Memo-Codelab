package com.kamikadze328.memo.view.create.location

import android.os.Parcelable
import com.kamikadze328.memo.model.MemoLocation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseLocationArgs(
    val location: MemoLocation?,
    val canChooseLocation: Boolean = true,
): Parcelable