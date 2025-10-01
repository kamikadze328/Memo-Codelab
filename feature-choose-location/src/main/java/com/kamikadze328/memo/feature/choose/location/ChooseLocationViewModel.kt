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

    private val _data = MutableStateFlow(DEFAULT_LOCATION)
    val data: StateFlow<MemoLocation> = _data.asStateFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        _data.value = MemoLocation(latitude = latitude, longitude = longitude)
    }

    fun initArgs(args: ChooseLocationArgs) {
        _data.value = args.location ?: return
    }
}