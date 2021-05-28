package com.ahmedtikiwa.fxapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ahmedtikiwa.fxapp.database.DatabaseCurrencies
import com.ahmedtikiwa.fxapp.database.FXAppDao
import com.ahmedtikiwa.fxapp.database.asDomainModel
import com.ahmedtikiwa.fxapp.domain.Conversion
import com.ahmedtikiwa.fxapp.domain.Currency
import com.ahmedtikiwa.fxapp.network.FXMarketNetwork
import com.ahmedtikiwa.fxapp.network.models.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FXAppRepository constructor(
    private val fxAppDao: FXAppDao
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _conversion = MutableLiveData<Conversion>()
    val conversion: LiveData<Conversion> = _conversion

    val currencies: LiveData<List<Currency>> = Transformations.map(fxAppDao.getCurrencies()) {
        it.asDomainModel()
    }

    suspend fun getConversion(from: String?, to: String?) {
        if (from.isNullOrBlank() || to.isNullOrBlank()) {
            return
        }

        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val response = FXMarketNetwork.fxMarketApi.getConversionAsync(from, to).await()
                _conversion.postValue(response.asDomainModel())
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Timber.d(e)
            }
        }
    }

    suspend fun refreshCurrencies() {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                // get the list of currencies from the network
                val response = FXMarketNetwork.fxMarketApi.getCurrenciesAsync().await()
                if (response.currencies.isNotEmpty()) {
                    // array to hold the split pairs
                    val currencyPairsList = mutableListOf<String>()
                    for ((key, _) in response.currencies) {
                        // if the key is a currency pair and not a currency
                        if (key.length == 6) {
                            currencyPairsList.add(key.substring(0, 3))
                            currencyPairsList.add(key.substring(3, key.length))
                            // else if the key is normal currency code length
                        } else if (key.length == 3) {
                            currencyPairsList.add(key)
                        }
                    }
                    val currenciesToInsert = mutableListOf<DatabaseCurrencies>()
                    for ((index, code) in currencyPairsList.distinct().withIndex()) {
                        currenciesToInsert.add(DatabaseCurrencies(index + 1, code))
                    }
                    fxAppDao.apply {
                        deleteAllCurrencies()
                        insertAllCurrencies(*currenciesToInsert.toTypedArray())
                    }
                }
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Timber.d(e)
            }
        }
    }
}