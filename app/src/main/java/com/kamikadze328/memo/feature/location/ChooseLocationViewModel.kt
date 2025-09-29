package com.kamikadze328.memo.feature.location

import androidx.lifecycle.ViewModel
import com.kamikadze328.memo.domain.model.MemoLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ChooseLocationViewModel : ViewModel() {
    companion object {
        private val BATUMI_LOCATION = MemoLocation(41.6413, 41.6359)
        private val DEFAULT_LOCATION = BATUMI_LOCATION
    }

    private val _data = MutableStateFlow(DEFAULT_LOCATION)
    val data = _data.asStateFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        _data.value = MemoLocation(latitude = latitude, longitude = longitude)
    }

    fun initArgs(location: MemoLocation?) {
        _data.value = location ?: return
    }
}