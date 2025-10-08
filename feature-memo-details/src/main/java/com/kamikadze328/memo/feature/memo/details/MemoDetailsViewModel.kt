package com.kamikadze328.memo.feature.memo.details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.IMemoRepository
import com.kamikadze328.memo.navigation.memo.details.MemoDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
class MemoDetailsViewModel @Inject constructor(
    private val repository: IMemoRepository,
    @param:ApplicationContext
    private val applicationContext: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var _uiState = MutableStateFlow(MemoDetailsUiState.EMPTY)
    val uiState: StateFlow<MemoDetailsUiState> = _uiState.asStateFlow()

    init {
        initArgs(args = MemoDetailsArgs.from(savedStateHandle))
    }

    fun initArgs(args: MemoDetailsArgs) {
        when (val openingType = args.openingType) {
            is MemoDetailsArgs.OpeningType.CreateNew -> {
                _uiState.value = MemoDetailsUiState.EMPTY.copy(
                    canEdit = true,
                    memo = Memo.EMPTY,
                )
            }

            is MemoDetailsArgs.OpeningType.View -> {
                viewModelScope.launch {
                    _uiState.value = MemoDetailsUiState.EMPTY.copy(
                        canEdit = false,
                        memo = repository.getMemoById(openingType.memoId),
                    )
                }
            }
        }
    }

    /**
     * Saves the memo in it's current state.
     */
    fun tryToSaveMemo() {
        if (!isValid()) {
            updateErrorTexts()
            return
        }

        viewModelScope.launch {
            repository.saveMemo(_uiState.value.memo)

            _uiState.value = _uiState.value.copy(
                shouldFinish = true,
            )
        }
    }

    private fun updateErrorTexts() {
        val memo = _uiState.value.memo
        val titleError = if (memo.title.isBlank()) {
            applicationContext.getString(R.string.memo_title_empty_error)
        } else {
            null
        }
        val textError = if (memo.description.isBlank()) {
            applicationContext.getString(R.string.memo_text_empty_error)
        } else {
            null
        }
        val locationError = if (memo.reminderLocation == null) {
            applicationContext.getString(R.string.memo_location_empty_error)
        } else {
            null
        }
        _uiState.value = _uiState.value.copy(
            titleError = titleError,
            textError = textError,
            locationError = locationError,
        )
    }

    fun onNewTitleValue(title: String?) {
        _uiState.value = _uiState.value.copy(
            memo = _uiState.value.memo.copy(
                title = title.orEmpty(),
            )
        )
    }

    fun onNewDescriptionValue(description: String?) {
        _uiState.value = _uiState.value.copy(
            memo = _uiState.value.memo.copy(
                description = description.orEmpty(),
            )
        )
    }

    fun onNewMemoLocation(location: MemoLocation?) {
        _uiState.value = _uiState.value.copy(
            memo = _uiState.value.memo.copy(
                reminderLocation = location,
            )
        )
    }

    private fun isValid(): Boolean {
        val memo = _uiState.value.memo

        return memo.title.isNotBlank() && memo.description.isNotBlank() && memo.reminderLocation != null
    }
}