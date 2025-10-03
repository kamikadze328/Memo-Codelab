package com.kamikadze328.memo.feature.home

import androidx.compose.runtime.Immutable
import com.kamikadze328.memo.domain.model.Memo

@Immutable
data class HomeUiState(
    val memos: List<Memo>,
    val shouldShowAll: Boolean,
) {
    companion object {
        val EMPTY = HomeUiState(
            memos = listOf(),
            shouldShowAll = false,
        )
    }
}