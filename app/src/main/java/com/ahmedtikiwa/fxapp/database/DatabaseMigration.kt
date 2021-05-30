package com.ahmedtikiwa.fxapp.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY NOT NULL, date TEXT NOT NULL, currencyPair TEXT NOT NULL, price REAL NOT NULL)")
    }
}