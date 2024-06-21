package com.example.shopify_app.core.networking

import com.example.shopify_app.core.models.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyServices {
    @GET("convert")
    suspend fun getConversionRate(@Query("from") base : String , @Query("to") to : String ,@Query("api_key") apiKey : String = RetrofitHelper.CURRENCY_API_KEY) : ConversionResponse
}