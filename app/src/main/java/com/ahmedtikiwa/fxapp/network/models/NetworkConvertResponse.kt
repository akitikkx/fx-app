package com.ahmedtikiwa.fxapp.network.models

import com.ahmedtikiwa.fxapp.domain.Conversion

data class NetworkConvertResponse(
    val from: String?,
    val price: Double?,
    val timestamp: Int?,
    val to: String?,
    val total: Double?
)

fun NetworkConvertResponse.asDomainModel() : Conversion {
    return Conversion(
        from = from,
        to = to,
        timestamp = timestamp,
        price = price,
        total = total
    )
}