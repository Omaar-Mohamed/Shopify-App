package com.example.shopify_app.core.networking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.android.AndroidLogHandler.setLevel
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    private const val BASE_URL = "https://28d3ccbaf671ad8fbfda834bcce48553:a53366a9bb6740eda424da4bd4f18589@mad44-alex-android.myshopify.com/"
    private const val CURRENCY_BASE_URL = "https://api.getgeoapi.com/v2/currency/"
    const val CURRENCY_API_KEY = "26c960f1a769e1deed38f42d0299e5c9d496a76a"
    private const val ACCESS_TOKEN = "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" // Replace with your actual access token
    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    // Create an interceptor to add the header
    private val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("X-Shopify-Access-Token", ACCESS_TOKEN)
        val request = requestBuilder.build()
        chain.proceed(request)
    }
    // Create OkHttpClient and add the interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
//        .addInterceptor(logging)
        .build()

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // Create the Retrofit instance with the OkHttpClient
    val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val retrofitCurrency: Retrofit = Retrofit.Builder()
        .baseUrl(CURRENCY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

