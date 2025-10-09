package com.kamikadze328.memo.navigation.memo.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.kamikadze328.memo.navigation.MemoDetails
import com.kamikadze328.memo.navigation.serializableType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class MemoDetailsArgs(
    val openingType: OpeningType,
) {
    companion object {
        val typeMap = mapOf(typeOf<MemoDetailsArgs>() to serializableType<MemoDetailsArgs>())

        fun from(savedStateHandle: SavedStateHandle): MemoDetailsArgs {
            return savedStateHandle.toRoute<MemoDetails>(typeMap).args
        }
    }

    @Serializable
    sealed interface OpeningType {
        @Serializable
        data object CreateNew : OpeningType

        @Serializable
        data class View(val memoId: Long) : OpeningType
    }
}