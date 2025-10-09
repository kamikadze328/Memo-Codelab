package com.kamikadze328.memo.navigation.choose.location

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.navigation.ChooseLocation
import com.kamikadze328.memo.navigation.serializableType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ChooseLocationArgs(
    val location: MemoLocation?,
    val canChooseLocation: Boolean = true,
) {
    companion object {
        val typeMap = mapOf(typeOf<ChooseLocationArgs>() to serializableType<ChooseLocationArgs>())

        fun from(savedStateHandle: SavedStateHandle): ChooseLocationArgs {
            return savedStateHandle.toRoute<ChooseLocation>(typeMap).args
        }
    }
}