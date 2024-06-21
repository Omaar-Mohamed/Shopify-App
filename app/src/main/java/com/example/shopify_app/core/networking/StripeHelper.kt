package com.example.shopify_app.core.networking

import com.example.shopify_app.core.utils.SECRET_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.HttpException
import com.google.gson.Gson
import com.google.gson.GsonBuilder

    data class PaymentIntentRequest(val amount: Long, val currency: String)
    data class PaymentIntentResponse(val client_secret: String, val id: String)

class AuthInterceptor(private val secretKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .header("Authorization", "Bearer $secretKey")
            .build()
        return chain.proceed(request)
    }
}

interface StripeService {
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): PaymentIntentResponse
}

// Create a Gson instance for serialization
val gson: Gson = GsonBuilder().create()

// Create an OkHttpClient with the interceptor
val client = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(SECRET_KEY))
    .build()

// Create a Retrofit instance
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.stripe.com/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

val stripeService: StripeService = retrofit.create(StripeService::class.java)
