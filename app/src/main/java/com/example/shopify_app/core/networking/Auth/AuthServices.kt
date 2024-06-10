package com.example.shopify_app.core.networking.Auth

import com.example.shopify_app.features.signup.data.model.Customer
import com.example.shopify_app.features.signup.data.model.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthServices {
    @POST("customers.json")
    suspend fun signUpCustomer(
        @Body body: SignupRequest,
        @Header("X-Shopify-Access-Token") password: String = "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",
    ): Response<Customer>

    @GET("customers.json")
    suspend fun logInCustomers(
        @Query("email") email: String,
        @Header("X-Shopify-Access-Token") passwordToken: String = "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"
    ): Response<List<Customer>>


    @PUT("customers/{id}.json")
    suspend fun updateCustomer(
        @Path("id") id: String,
        @Body customer: Customer,
        @Header("X-Shopify-Access-Token") password: String = "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"
    ): Response<Customer>
}