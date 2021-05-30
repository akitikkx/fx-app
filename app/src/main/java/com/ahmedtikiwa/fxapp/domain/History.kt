package com.ahmedtikiwa.fxapp.domain

data class History(
    val id: Int,
    val date: String,
    val currencyPair: String,
    val price: Double
)