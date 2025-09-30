package com.kamikadze328.memo.feature.memo.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
internal class CreateMemoViewModel @Inject constructor(
    private val repository: IMemoRepository,
) : ViewModel() {

    private var memo = Memo(
        id = 0,
        title = "",
        description = "",
        reminderDate = 0,
        reminderLocation = null,
        isDone = false,
    )

    /**
     * Saves the memo in it's current state.
     */
    fun saveMemo() {
        viewModelScope.launch {
            repository.saveMemo(memo)
        }
    }

    /**
     * Call this method to update the memo. This is usually needed when the user changed his input.
     */
    fun updateMemo(title: String, description: String) {
        memo = Memo(
            title = title,
            description = description,
            id = 0,
            reminderDate = 0,
            reminderLocation = null,
            isDone = false,
        )
    }

    fun updateMemoLocation(location: MemoLocation?) {
        memo = memo.copy(
            reminderLocation = location,
        )
    }

    fun getMemoLocation(): MemoLocation? {
        return memo.reminderLocation
    }

    /**
     * @return true if the title and content are not blank; false otherwise.
     */
    fun isMemoValid(): Boolean = memo.title.isNotBlank() && memo.description.isNotBlank()

    /**
     * @return true if the memo text is blank, false otherwise.
     */
    fun hasTextError() = memo.description.isBlank()

    /**
     * @return true if the memo title is blank, false otherwise.
     */
    fun hasTitleError() = memo.title.isBlank()
}