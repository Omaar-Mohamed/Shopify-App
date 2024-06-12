package com.example.shopify_app.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.login.data.LoginRepo
import com.example.shopify_app.features.signup.data.repo.SignupRepo
import com.example.shopify_app.features.signup.viewmodel.SignupViewModel

class LoginViewModelFactory  (
    private val repository: LoginRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}