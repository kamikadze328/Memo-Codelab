package com.kamikadze328.memo.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
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
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.EMPTY)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        refreshMemos()
    }

    fun onShowAllClick() {
        _uiState.value = _uiState.value.copy(shouldShowAll = true)

        refreshMemos()
    }

    fun onShowOpenClick() {
        _uiState.value = _uiState.value.copy(shouldShowAll = false)

        refreshMemos()
    }

    /**
     * Loads all memos.
     */
    private fun loadAllMemos() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    memos = memoRepository.getAll()
                )
            } catch (e: Throwable) {
                ensureActive()
                Log.e(TAG, "Failed to load memos", e)
            }
        }
    }

    /**
     * Loads all open (not done) memos.
     */
    private fun loadOpenMemos() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    memos = memoRepository.getOpen()
                )
            } catch (e: Throwable) {
                ensureActive()
                Log.e(TAG, "Failed to load opened memos", e)
            }
        }
    }

    fun refreshMemos() {
        if (_uiState.value.shouldShowAll) {
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
    fun onUpdateMemo(memo: Memo, isChecked: Boolean) {
        // We'll only forward the update if the memo has been checked, since we don't offer to uncheck memos right now
        if (!isChecked) return

        viewModelScope.launch {
            memoRepository.saveMemo(memo.copy(isDone = true))

            refreshMemos()
        }
    }
}