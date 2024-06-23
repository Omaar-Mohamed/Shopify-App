package com.example.shopify_app.core.networking


import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface StripeServices {
    @FormUrlEncoded
    @POST("/v1/checkout/sessions")
    suspend fun createSession(
        @Field("customer_email") email : String,
        @Field("success_url") successUrl: String,
        @Field("cancel_url") cancelUrl: String,
        @Field("mode") mode: String,
        @FieldMap(encoded = true) lineItems: Map<String, String>
    ): CheckoutResponse
}
