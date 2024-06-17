package com.example.shopify_app.features.ProductDetails.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo

class DraftViewModelFactory  (
    private val repository: ProductsDetailsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DraftViewModel::class.java)) {
            DraftViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}