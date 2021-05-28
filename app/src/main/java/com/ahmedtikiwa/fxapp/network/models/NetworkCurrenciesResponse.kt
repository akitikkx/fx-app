package com.ahmedtikiwa.fxapp.network.models

import com.google.gson.internal.LinkedTreeMap

data class NetworkCurrenciesResponse(
    val currencies: LinkedTreeMap<String,String>
)