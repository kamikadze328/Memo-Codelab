package com.kamikadze328.memo.feature.memo.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching ViewMemo view.
 */
@HiltViewModel
internal class ViewMemoViewModel @Inject constructor(
    private val memoRepository: IMemoRepository
) : ViewModel() {

    private val _memo: MutableStateFlow<Memo?> = MutableStateFlow(null)
    val memo: StateFlow<Memo?> = _memo

    /**
     * Loads the memo whose id matches the given memoId from the database.
     */
    fun loadMemo(memoId: Long) {
        viewModelScope.launch {
            _memo.value = memoRepository.getMemoById(memoId)
        }
    }
}