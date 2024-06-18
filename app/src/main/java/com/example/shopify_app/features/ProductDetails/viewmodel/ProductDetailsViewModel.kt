package com.example.shopify_app.features.ProductDetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val repository: ProductsDetailsRepo
) : ViewModel() {

    private val _product: MutableStateFlow<ApiState<ProductDetailResponse>> = MutableStateFlow(ApiState.Loading)
    val product: StateFlow<ApiState<ProductDetailResponse>> = _product

    fun getProductDetails(id: String){
        viewModelScope.launch {
            repository.getProductsDetails(id)
                .catch { e -> _product.value = ApiState.Failure(e) }
                .collect { data -> _product.value = ApiState.Success(data) }
        }
    }

}