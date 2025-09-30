package com.kamikadze328.memo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home Activity.
 */
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val memoRepository: IMemoRepository
) : ViewModel() {

    private var isShowAll = false
    private val _memos: MutableStateFlow<List<Memo>> = MutableStateFlow(listOf())
    val memos: StateFlow<List<Memo>> = _memos.asStateFlow()

    /**
     * Loads all memos.
     */
    fun loadAllMemos() {
        isShowAll = true
        viewModelScope.launch {
            _memos.value = memoRepository.getAll()
        }
    }

    /**
     * Loads all open (not done) memos.
     */
    fun loadOpenMemos() {
        isShowAll = false
        viewModelScope.launch {
            _memos.value = memoRepository.getOpen()
        }
    }

    fun refreshMemos() {
        if (isShowAll) {
            loadAllMemos()
        } else {
            loadOpenMemos()
        }
    }

    /**
     * Updates the given memo, marking it as done if isChecked is true.
     *
     * @param memo      - the memo to update.
     * @param isChecked - whether the memo has been checked (marked as done).
     */
    fun updateMemo(memo: Memo, isChecked: Boolean) {
        viewModelScope.launch {
            // We'll only forward the update if the memo has been checked, since we don't offer to uncheck memos right now
            if (isChecked) {
                memoRepository.saveMemo(memo.copy(isDone = true))
            }
        }
    }
}