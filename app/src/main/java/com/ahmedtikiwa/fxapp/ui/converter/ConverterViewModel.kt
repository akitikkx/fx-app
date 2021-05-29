package com.ahmedtikiwa.fxapp.ui.converter

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.ahmedtikiwa.fxapp.repository.FXAppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FXAppRepository
) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _rateRequested = MutableLiveData<Boolean>()
    val rateRequested: LiveData<Boolean> = _rateRequested

    val isLoading = repository.isLoading

    val currenciesList = repository.currencies

    val conversion = repository.conversion

    val error = repository.error

    init {
        viewModelScope.launch {
            repository.getConversion(
                savedStateHandle.get<String>(FROM_CURRENCY), savedStateHandle.get<String>(
                    TO_CURRENCY
                )
            )
        }
    }

    fun onGetRateButtonClick() {
        _rateRequested.postValue(true)
    }

    fun onRateRequestCompleted() {
        _rateRequested.postValue(false)
    }

    fun onRateQueryReceived(from: String, to: String) {
        savedStateHandle.set(FROM_CURRENCY, from)
        savedStateHandle.set(TO_CURRENCY, to)

        viewModelScope.launch {
            repository.getConversion(
                savedStateHandle.get<String>(FROM_CURRENCY), savedStateHandle.get<String>(
                    TO_CURRENCY
                )
            )
        }
    }

    @AssistedFactory
    interface ConverterViewModelFactory {
        fun create(
            owner: SavedStateRegistryOwner
        ): Factory
    }

    companion object {
        const val FROM_CURRENCY = "from_currency"
        const val TO_CURRENCY = "to_currency"
    }

    class Factory @AssistedInject constructor(
        @Assisted owner: SavedStateRegistryOwner,
        private val repository: FXAppRepository
    ) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return ConverterViewModel(handle, repository) as T
        }
    }
}