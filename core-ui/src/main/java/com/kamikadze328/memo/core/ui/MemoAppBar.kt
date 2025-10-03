package com.kamikadze328.memo.core.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun MemoAppBar(
    title: String,
    actionTitle: String? = null,
    onActionClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        actions = {
            actionTitle?.let {
                TextButton(onClick = onActionClick) {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}