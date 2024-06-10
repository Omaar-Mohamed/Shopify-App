package com.example.shopify_app.features.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.AuthState
import com.example.shopify_app.features.signup.data.repo.SignupRepoImpl
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val signupRepository = SignupRepoImpl()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val _emailVerificationState = MutableLiveData<Boolean>()
    val emailVerificationState: LiveData<Boolean> get() = _emailVerificationState

    fun getCurrentUser() {
        _authState.value = AuthState.Success(signupRepository.currentUser)
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = signupRepository.signup(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            try {
                val result = signupRepository.sendEmailVerification()
                _emailVerificationState.value = result
            } catch (e: Exception) {
                _emailVerificationState.value = false
            }
        }
    }
}