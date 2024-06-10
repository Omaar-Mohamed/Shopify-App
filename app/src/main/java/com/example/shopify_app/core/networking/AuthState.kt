package com.example.shopify_app.core.networking

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val exception: Exception?) : AuthState()
}