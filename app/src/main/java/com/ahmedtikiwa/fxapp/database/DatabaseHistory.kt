package com.ahmedtikiwa.fxapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class DatabaseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val currencyPair: String,
    val price: Double
)