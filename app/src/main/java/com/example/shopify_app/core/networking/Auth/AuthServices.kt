package com.example.shopify_app.core.networking.Auth


import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthServices {
    @POST("customers.json")
    suspend fun signUpCustomer(
        @Body body: SignupRequest,
    ): CustomerRespones

    @GET("customers.json")
    suspend fun getCustomer(
        @Query("email") email: String,
    ): CustomerRespones

    @PUT("customers/{id}.json")
    suspend fun updateCustomer(
        @Path("id") id: String,
        @Body customer: UpdateCustomer,
    ): CustomerRespones
}