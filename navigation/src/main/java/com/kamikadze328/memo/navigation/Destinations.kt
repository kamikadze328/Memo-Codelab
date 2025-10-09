package com.kamikadze328.memo.navigation

import com.kamikadze328.memo.navigation.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.navigation.memo.details.MemoDetailsArgs
import kotlinx.serialization.Serializable


@Serializable
data object Home

@Serializable
data class MemoDetails(val args: MemoDetailsArgs)
@Serializable
data object MemoDetailsDeepLink

@Serializable
data class ChooseLocation(val args: ChooseLocationArgs)