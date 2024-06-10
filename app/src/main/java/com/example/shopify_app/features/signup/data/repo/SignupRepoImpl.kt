package com.example.shopify_app.features.signup.data.repo

import com.example.shopify_app.features.signup.data.repo.SignupRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class SignupRepoImpl : SignupRepo {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signup(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun sendEmailVerification(): Boolean {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            user.sendEmailVerification().await()
            true
        } else {
            false
        }
    }
}