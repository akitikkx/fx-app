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
}