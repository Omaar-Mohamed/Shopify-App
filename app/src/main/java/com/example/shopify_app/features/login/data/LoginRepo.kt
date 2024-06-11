package com.example.shopify_app.features.login.data

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun isEmailVerified(): Boolean
    suspend fun signInWithGoogle(idToken: String): FirebaseUser?
}