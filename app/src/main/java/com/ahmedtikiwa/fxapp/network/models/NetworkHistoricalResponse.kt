package com.ahmedtikiwa.fxapp.network.models

import com.google.gson.internal.LinkedTreeMap

data class NetworkHistoricalResponse(
    val date: String,
    val price: LinkedTreeMap<String, Double>
)