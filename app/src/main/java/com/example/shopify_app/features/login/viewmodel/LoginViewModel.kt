package com.example.shopify_app.features.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.login.data.LoginRepo
import com.example.shopify_app.features.login.data.LoginRepoImpl
import com.example.shopify_app.features.signup.data.repo.SignupRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: LoginRepo
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val _isEmailVerifiedState = MutableLiveData<Boolean>()
    val isEmailVerifiedState: LiveData<Boolean> get() = _isEmailVerifiedState

    private val _customer: MutableStateFlow<ApiState<LoginCustomer>> = MutableStateFlow(ApiState.Loading)
    val customer: StateFlow<ApiState<LoginCustomer>> = _customer

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.login(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            try {
                val result = repository.isEmailVerified()
                _isEmailVerifiedState.value = result
            } catch (e: Exception) {
                _isEmailVerifiedState.value = false
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.signInWithGoogle(idToken)

                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    suspend fun getCustomer(email: String) : LoginCustomer{
        return repository.getCustomerByEmail(email)
    }
}