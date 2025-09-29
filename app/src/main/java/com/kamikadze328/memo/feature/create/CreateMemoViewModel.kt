package com.kamikadze328.memo.feature.create

import androidx.lifecycle.ViewModel
import com.kamikadze328.memo.core.android.coroutines.ScopeProvider
import com.kamikadze328.memo.core.common.extensions.empty
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.Repository
import kotlinx.coroutines.launch

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
internal class CreateMemoViewModel : ViewModel() {

    private var memo = Memo(0, String.empty(), String.empty(), 0, 0, 0, false)

    /**
     * Saves the memo in it's current state.
     */
    fun saveMemo() {
        ScopeProvider.application.launch {
            Repository.saveMemo(memo)
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
            reminderLatitude = 0,
            reminderLongitude = 0,
            isDone = false,
            location = memo.location?.copy(),
        )
    }

    fun updateMemoLocation(location: MemoLocation?) {
        memo = memo.copy(
            location = location,
        )
    }

    fun getMemoLocation(): MemoLocation? {
        return memo.location
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