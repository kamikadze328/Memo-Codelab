package com.kamikadze328.memo.feature.memo.details

import androidx.compose.runtime.Immutable
import com.kamikadze328.memo.domain.model.Memo

@Immutable
data class MemoDetailsUiState(
    val canEdit: Boolean,
    val memo: Memo,
    val titleError: String?,
    val textError: String?,
    val locationError: String?,
    val shouldFinish: Boolean,
) {
    companion object {
        val EMPTY = MemoDetailsUiState(
            canEdit = false,
            memo = Memo.Companion.EMPTY,
            titleError = null,
            textError = null,
            locationError = null,
            shouldFinish = false,
        )
    }
}