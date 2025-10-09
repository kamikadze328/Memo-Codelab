package com.kamikadze328.memo.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState

inline fun <reified T : Any> NavController.setResult(
    value: T,
    key: String = T::class.java.name,
) {
    previousBackStackEntry?.savedStateHandle?.set(key, encodeToSavedState(value))
}

@Composable
inline fun <reified T : Any> NavController.CollectResult(
    key: String = T::class.java.name,
    crossinline onValueChange: (T) -> Unit,
) {
    val resultFlow = currentBackStackEntry?.savedStateHandle?.getStateFlow<Bundle?>(key, null)
        ?.collectAsStateWithLifecycle()
    val result = resultFlow?.value?.let { decodeFromSavedState<T>(it) }

    LaunchedEffect(result) {
        if (result != null) {
            onValueChange(result)
            currentBackStackEntry?.savedStateHandle?.remove<Bundle>(key)
        }
    }
}