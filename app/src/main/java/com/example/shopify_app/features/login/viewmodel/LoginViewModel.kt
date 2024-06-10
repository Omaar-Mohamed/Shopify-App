package com.example.shopify_app.features.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.login.data.LoginRepoImpl
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepoImpl()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val _isEmailVerifiedState = MutableLiveData<Boolean>()
    val isEmailVerifiedState: LiveData<Boolean> get() = _isEmailVerifiedState
    fun getCurrentUser() {
        _authState.value = AuthState.Success(loginRepository.currentUser)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = loginRepository.login(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            try {
                val result = loginRepository.isEmailVerified()
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
                val user = loginRepository.signInWithGoogle(idToken)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }
}