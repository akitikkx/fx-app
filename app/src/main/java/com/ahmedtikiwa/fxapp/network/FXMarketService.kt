package com.ahmedtikiwa.fxapp.network

import com.ahmedtikiwa.fxapp.network.models.NetworkConvertResponse
import com.ahmedtikiwa.fxapp.network.models.NetworkCurrenciesResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FXMarketService {

    @GET("apicurrencies")
    fun getCurrenciesAsync(): Deferred<NetworkCurrenciesResponse>

    @GET("apiconvert")
    fun getConversionAsync(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Int = 1,
    ): Deferred<NetworkConvertResponse>
}

object FXMarketNetwork {
    private const val BASE_URL = "https://fxmarketapi.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(FXMarketConnectionInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val fxMarketApi: FXMarketService = retrofit.create(FXMarketService::class.java)
}