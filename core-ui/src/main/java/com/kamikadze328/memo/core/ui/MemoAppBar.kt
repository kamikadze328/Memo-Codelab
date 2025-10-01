package com.kamikadze328.memo.core.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun MemoAppBar(
    title: String,
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}