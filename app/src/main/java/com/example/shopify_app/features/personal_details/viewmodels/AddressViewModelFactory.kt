package com.example.shopify_app.features.personal_details.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.products.data.repo.ProductsRepo
import com.example.shopify_app.features.products.viewmodel.ProductsViewModel

class AddressViewModelFactory(
    private val repository: PersonalRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            AddressViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}