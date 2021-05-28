package com.ahmedtikiwa.fxapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DatabaseCurrencies::class
    ],
    version = 1,
    exportSchema = true
)

abstract class FXAppDatabase : RoomDatabase() {
    abstract val fxAppDao: FXAppDao
}