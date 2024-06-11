package com.example.shopify_app.features.signup.data.repo

import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import com.google.firebase.auth.FirebaseUser

interface SignupRepo {
    suspend fun signup(email: String, password: String): FirebaseUser?
    suspend fun sendEmailVerification(): Boolean
    suspend fun registerUserInApi(signupRequest: SignupRequest) : CustomerRespones
    suspend fun updateCustomer(id: String, updateCustomer: UpdateCustomer): CustomerRespones
    suspend fun createDraftOrder(draftOderRequest: DraftOderRequest) : DraftOrderResponse
}