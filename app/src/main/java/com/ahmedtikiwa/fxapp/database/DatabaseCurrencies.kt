package com.ahmedtikiwa.fxapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedtikiwa.fxapp.domain.Currency

@Entity(tableName = "currencies")
data class DatabaseCurrencies(
    @PrimaryKey
    val id: Int,
    val code: String
)

fun List<DatabaseCurrencies>.asDomainModel(): List<Currency> {
    return map {
        Currency(
            id = it.id,
            code = it.code
        )
    }
}