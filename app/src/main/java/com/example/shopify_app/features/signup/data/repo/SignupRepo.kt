package com.example.shopify_app.features.signup.data.repo

import com.google.firebase.auth.FirebaseUser

interface SignupRepo {
    suspend fun signup(email: String, password: String): FirebaseUser?
    suspend fun sendEmailVerification(): Boolean
}