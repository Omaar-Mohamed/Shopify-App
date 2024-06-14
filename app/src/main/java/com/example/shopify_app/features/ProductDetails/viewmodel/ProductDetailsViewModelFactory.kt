package com.example.shopify_app.features.ProductDetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo

class ProductDetailsViewModelFactory  (
    private val repository: ProductsDetailsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            ProductDetailsViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}