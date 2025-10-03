package com.kamikadze328.memo.feature.choose.location

import androidx.lifecycle.ViewModel
import com.kamikadze328.memo.domain.model.MemoLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class ChooseLocationViewModel @Inject constructor() : ViewModel() {
    private val _data = MutableStateFlow(ChooseLocationUiState.EMPTY)
    val data: StateFlow<ChooseLocationUiState> = _data.asStateFlow()

    fun updateLocation(memoLocation: MemoLocation) {
        _data.value = _data.value.copy(
            location = memoLocation
        )
    }

    fun initArgs(args: ChooseLocationArgs) {
        _data.value = _data.value.copy(
            canChoose = args.canChooseLocation,
            initialLocation = args.location,
            location = args.location ?: ChooseLocationUiState.DEFAULT_LOCATION,
        )
    }
}