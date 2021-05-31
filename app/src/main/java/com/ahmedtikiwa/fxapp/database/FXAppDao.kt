package com.ahmedtikiwa.fxapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FXAppDao {

    @Query("delete from currencies")
    fun deleteAllCurrencies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCurrencies(vararg currencies: DatabaseCurrencies)

    @Query("select * from currencies")
    fun getCurrencies() : LiveData<List<DatabaseCurrencies>>

    @Query("delete from history")
    fun deleteAllHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllHistory(vararg history: DatabaseHistory)

    @Query("select * from history where currencyPair = :currencyPair ORDER BY date ASC")
    fun getCurrencyPairHistory(currencyPair: String): LiveData<List<DatabaseHistory>>

    @Query("select * from history")
    fun getPagedHistory(): List<DatabaseHistory>
}