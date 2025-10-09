package com.kamikadze328.memo.navigation.choose.location

import com.kamikadze328.memo.domain.model.MemoLocation
import kotlinx.serialization.Serializable

@Serializable
data class ChooseLocationResult(
    val location: MemoLocation?,
)