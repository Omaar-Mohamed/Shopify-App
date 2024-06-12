package com.example.shopify_app.features.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.categories.data.repo.CategoriesRepo
import com.example.shopify_app.features.home.viewmodel.HomeViewModel

class CategoriesViewModelFactory (
    private val categoriesRepo: CategoriesRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            CategoriesViewModel(categoriesRepo) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}