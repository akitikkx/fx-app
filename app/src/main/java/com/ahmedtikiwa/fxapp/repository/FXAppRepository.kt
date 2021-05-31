package com.ahmedtikiwa.fxapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ahmedtikiwa.fxapp.database.DatabaseCurrencies
import com.ahmedtikiwa.fxapp.database.DatabaseHistory
import com.ahmedtikiwa.fxapp.database.FXAppDao
import com.ahmedtikiwa.fxapp.database.asDomainModel
import com.ahmedtikiwa.fxapp.domain.Conversion
import com.ahmedtikiwa.fxapp.domain.Currency
import com.ahmedtikiwa.fxapp.domain.History
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val currencies: LiveData<List<Currency>> = Transformations.map(fxAppDao.getCurrencies()) {
        it.asDomainModel()
    }

    fun currencyHistory(currencyPair: String): LiveData<List<History>> =
        Transformations.map(fxAppDao.getCurrencyPairHistory(currencyPair)) {
            it.asDomainModel()
        }

    fun getHistory() =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                initialLoadSize = 30
            ),
            pagingSourceFactory = { HistoryPagingSource(fxAppDao) }
        ).flow

    /**
     * Get the current FX Rate for the given currencies
     */
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
                _error.postValue(e.message)
                Timber.d(e)
            }
        }
    }

    /**
     * Store the currently supported list of currencies from FX Market API
     */
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
                _error.postValue(e.message)
                Timber.d(e)
            }
        }
    }

    /**
     * Get the historical information for currencies pairs for a given date
     * Note: this only returns 3 currency pairs at a time and for a single date
     * and not a date range
     */
    suspend fun getHistorical(date: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val historicalResponse =
                    FXMarketNetwork.fxMarketApi.getHistoricalAsync(date).await()

                val historyToInsert = mutableListOf<DatabaseHistory>()
                if (historicalResponse.price.isNotEmpty()) {
                    for ((key, value) in historicalResponse.price) {
                        historyToInsert.add(
                            DatabaseHistory(
                                currencyPair = key,
                                price = value,
                                date = historicalResponse.date
                            )
                        )
                    }
                    if (historyToInsert.isNotEmpty()) {
                        fxAppDao.insertAllHistory(*historyToInsert.toTypedArray())
                    }
                }
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _error.postValue(e.message)
                Timber.d(e)
            }
        }
    }

    /**
     * Clear the currently saved history to be replaced with new historical data
     * This function is called by workManager
     */
    suspend fun clearHistory() {
        withContext(Dispatchers.IO) {
            fxAppDao.deleteAllHistory()
        }
    }
}