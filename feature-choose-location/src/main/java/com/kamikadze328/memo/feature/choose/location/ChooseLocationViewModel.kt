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
    companion object {
        private val BATUMI_LOCATION = MemoLocation(41.6413, 41.6359)
        private val DEFAULT_LOCATION = BATUMI_LOCATION
    }

    private val _data = MutableStateFlow(
        ChooseLocationUiState(
            location = DEFAULT_LOCATION,
            canChoose = false,
            initialLocation = DEFAULT_LOCATION
        )
    )
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
            location = args.location ?: DEFAULT_LOCATION,
        )
    }
}