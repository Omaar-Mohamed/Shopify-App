package com.example.shopify_app.features.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Customer
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOrder
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomerModel
import com.example.shopify_app.features.signup.data.repo.SignupRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val repository: SignupRepo
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val _emailVerificationState = MutableStateFlow<Boolean?>(null)
    val emailVerificationState: StateFlow<Boolean?> = _emailVerificationState

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.signup(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            try {
                val result = repository.sendEmailVerification()
                _emailVerificationState.value = result
            } catch (e: Exception) {
                _emailVerificationState.value = false
            }
        }
    }

    fun signUpApi(signupRequest: SignupRequest){
        viewModelScope.launch {
            try{
                val respones =  repository.registerUserInApi(signupRequest)
                Log.i("signup", "signUpApi: "+respones.customer.id)

                val favorite = repository.createDraftOrder(DraftOderRequest(
                    DraftOrder(Customer(respones.customer.id))))

                val order = repository.createDraftOrder(DraftOderRequest(
                    DraftOrder(Customer(respones.customer.id))))

                repository.updateCustomer(respones.customer.id.toString(),
                    UpdateCustomer(UpdateCustomerModel(
                        favorite.draft_order.id.toString(),order.draft_order.id.toString())))

            }catch (e : HttpException){
                Log.i("signup", "signUpApi: "+e.response.body.toString())
            }

        }
    }
}