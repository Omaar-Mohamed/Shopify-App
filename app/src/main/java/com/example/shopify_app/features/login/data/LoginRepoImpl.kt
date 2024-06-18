package com.example.shopify_app.features.login.data

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.signup.data.repo.SignupRepoImpl
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class LoginRepoImpl(
    private val appRemoteDataSourse: AppRemoteDataSourse
    ) : LoginRepo {

    companion object {
        private var instance: LoginRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): LoginRepoImpl {
            if (instance == null) {
                instance = LoginRepoImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun isEmailVerified(): Boolean {
        val user = firebaseAuth.currentUser
        return user?.reload()?.await().run { user?.isEmailVerified == true }
    }

    override suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        return authResult.user
    }
}