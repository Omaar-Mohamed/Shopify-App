package com.example.shopify_app.features.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.categories.data.repo.CategoriesRepo
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel(private val categoriesRepo: CategoriesRepo) : ViewModel() {
    private val _categories: MutableStateFlow<ApiState<CustomCategoriesResponse>> = MutableStateFlow(
        ApiState.Loading)
    val categories: MutableStateFlow<ApiState<CustomCategoriesResponse>> = _categories
    fun getCategories() {
        viewModelScope.launch {
            categoriesRepo.getCategories()
                .catch { e -> _categories.value = ApiState.Failure(e) }
                .collect { data -> _categories.value = ApiState.Success(data) }
        }
    }
}