package com.kamikadze328.memo.feature.memo.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.domain.repository.IMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
internal class CreateMemoViewModel @Inject constructor(
    private val repository: IMemoRepository,
) : ViewModel() {

    private var _memo = MutableStateFlow(Memo.EMPTY)
    val data: StateFlow<Memo> = _memo.asStateFlow()

    /**
     * Saves the memo in it's current state.
     */
    fun saveMemo() {
        viewModelScope.launch {
            repository.saveMemo(data.value)
        }
    }

    fun onNewTitleValue(title: String?) {
        _memo.value = _memo.value.copy(
            title = title.orEmpty(),
        )
    }

    fun onNewDescriptionValue(description: String?) {
        _memo.value = _memo.value.copy(
            description = description.orEmpty(),
        )
    }

    fun onNewMemoLocation(location: MemoLocation?) {
        _memo.value = _memo.value.copy(
            reminderLocation = location,
        )
    }

    /**
     * @return true if the title and content are not blank; false otherwise.
     */
    fun isMemoValid(): Boolean = !hasTextError() && !hasTitleError() && !hasLocationError()

    /**
     * @return true if the memo text is blank, false otherwise.
     */
    fun hasTextError() = data.value.description.isBlank()

    /**
     * @return true if the memo title is blank, false otherwise.
     */
    fun hasTitleError() = data.value.title.isBlank()

    /**
     * @return true if the memo location is null, false otherwise.
     */
    fun hasLocationError() = data.value.reminderLocation == null
}