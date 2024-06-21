package com.example.shopify_app.core.networking

import com.example.shopify_app.core.models.StripeCustomerResponse
import com.example.shopify_app.features.payment.data.StripeEphemeralKeyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StripeServices {
    @POST("v1/customers")
    suspend fun getCustomer() : Response<StripeCustomerResponse>
    @POST("v1/ephemeral_keys")
    suspend fun getEphemeral(
        @Query("customer") customerId: String): Response<StripeEphemeralKeyResponse>
    @POST("v1/payment_intents")
    suspend fun getPaymentIntent(
        @Query("customer") customerId: String,
        @Query("amount") amount : String,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") automatic : Boolean = true,
    ) : Response<PaymentIntentResponse>
}