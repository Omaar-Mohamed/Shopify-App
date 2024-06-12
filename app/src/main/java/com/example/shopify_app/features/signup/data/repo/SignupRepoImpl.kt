package com.example.shopify_app.features.signup.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class SignupRepoImpl(
    private val appRemoteDataSourse: AppRemoteDataSourse
) : SignupRepo {

    companion object {
        private var instance: SignupRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): SignupRepoImpl {
            if (instance == null) {
                instance = SignupRepoImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }

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
    override suspend fun registerUserInApi(signupRequest: SignupRequest) : CustomerRespones {
        return appRemoteDataSourse.signUpCustomer(signupRequest)
    }

    override suspend fun updateCustomer(
        id: String,
        updateCustomer: UpdateCustomer
    ): CustomerRespones {
        return appRemoteDataSourse.updateCustomer(id,updateCustomer)
    }

    override suspend fun createDraftOrder(draftOderRequest: DraftOderRequest): DraftOrderResponse {
        return appRemoteDataSourse.createDraftOrder(draftOderRequest)
    }
}