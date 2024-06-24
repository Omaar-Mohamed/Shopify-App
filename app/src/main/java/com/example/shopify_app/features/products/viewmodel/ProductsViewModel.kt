package com.example.shopify_app.features.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import com.example.shopify_app.features.products.data.repo.ProductsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductsRepo) : ViewModel() {
    private val _ProductsById: MutableStateFlow<ApiState<ProductsByIdResponse>> = MutableStateFlow(ApiState.Loading)
    val ProductsById: StateFlow<ApiState<ProductsByIdResponse>> = _ProductsById


//    fun getProductsById(collectionId: String) {
//        viewModelScope.launch {
//            repository.getProductsById(collectionId)
//                .catch { e -> _ProductsById.value = ApiState.Failure(e) }
//                .collect { data -> _ProductsById.value = ApiState.Success(data) }
//
//        }
//    }

    fun getProductsById(collectionId : String) {
        viewModelScope.launch {
            repository.getProductsById(collectionId)
                .catch { e -> _ProductsById.value = ApiState.Failure(e) }
                .collect { data -> _ProductsById.value = ApiState.Success(data) }

        }
    }
}