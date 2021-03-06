package com.ahmedtikiwa.fxapp.ui.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ahmedtikiwa.fxapp.repository.FXAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    repository: FXAppRepository
) : ViewModel() {

    val eurUsdHistory = repository.currencyHistory(EURUSD_PAIR)

    val gbpUsdHistory = repository.currencyHistory(GBPUSD_PAIR)

    val usdJpyHistory = repository.currencyHistory(USDJPY_PAIR)

    val history = repository.getHistory().cachedIn(viewModelScope)

    val isLoading = repository.isLoading

    companion object {
        const val EURUSD_PAIR = "EURUSD"
        const val GBPUSD_PAIR = "GBPUSD"
        const val USDJPY_PAIR = "USDJPY"
    }
}