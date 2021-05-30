package com.ahmedtikiwa.fxapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedtikiwa.fxapp.domain.History

@Entity(tableName = "history")
data class DatabaseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val currencyPair: String,
    val price: Double
)

fun List<DatabaseHistory>.asDomainModel(): List<History> {
    return map {
        History(
            id = it.id,
            date = it.date,
            currencyPair = it.currencyPair,
            price = it.price
        )
    }
}