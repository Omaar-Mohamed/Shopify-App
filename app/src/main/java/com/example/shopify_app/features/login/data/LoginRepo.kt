package com.example.shopify_app.features.login.data

import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginRepo {
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun isEmailVerified(): Boolean
    suspend fun signInWithGoogle(idToken: String): FirebaseUser?
    suspend fun getCustomerByEmail(email: String): LoginCustomer
}