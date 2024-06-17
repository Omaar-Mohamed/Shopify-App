package com.example.shopify_app.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.home.data.repo.HomeRepo
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepo) : ViewModel() {
    private val _priceRules: MutableStateFlow<ApiState<PriceRulesResponse>> = MutableStateFlow(ApiState.Loading)
    val priceRules: StateFlow<ApiState<PriceRulesResponse>> = _priceRules
    private val _smartCollections: MutableStateFlow<ApiState<SmartCollectionResponse>> = MutableStateFlow(ApiState.Loading)
    val smartCollections: StateFlow<ApiState<SmartCollectionResponse>> = _smartCollections

    private val _products: MutableStateFlow<ApiState<ProductsResponse>> = MutableStateFlow(ApiState.Loading)
    val products: StateFlow<ApiState<ProductsResponse>> = _products

    private val _customer: MutableStateFlow<ApiState<LoginCustomer>> = MutableStateFlow(ApiState.Loading)
    val customer: StateFlow<ApiState<LoginCustomer>> = _customer

    private val _favoriteProduct: MutableStateFlow<ApiState<DraftOrderResponse>> = MutableStateFlow(ApiState.Loading)
    val favoriteProduct: StateFlow<ApiState<DraftOrderResponse>> = _favoriteProduct
    fun getCustomer(email: String){
        viewModelScope.launch {
            repository.getCustomer(email)
                .catch { e -> _customer.value = ApiState.Failure(e) }
                .collect { data -> _customer.value = ApiState.Success(data) }
        }
    }

    fun getPriceRules() {
        viewModelScope.launch {
            repository.getPriceRules()
                .catch { e -> _priceRules.value = ApiState.Failure(e) }
                .collect { data -> _priceRules.value = ApiState.Success(data) }
        }
    }

    fun getSmartCollections() {
        viewModelScope.launch {
            repository.getSmartCollections()
                .catch { e -> _smartCollections.value = ApiState.Failure(e) }
                .collect { data -> _smartCollections.value = ApiState.Success(data) }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts()
                .catch { e -> _products.value = ApiState.Failure(e) }
                .collect { data ->
                    val shuffledProducts = data.products.shuffled().take(10)
                    _products.value = ApiState.Success(ProductsResponse(shuffledProducts))
                }
        }
    }

    fun insertFavProduct(id : String,product: Product){
        viewModelScope.launch{
            repository.getDraftOrder(id)
                .catch { e ->
                    e.printStackTrace()
                    _favoriteProduct.value = ApiState.Failure(e) }
                .collect { data ->
                    val response = data.draft_order
                    val newLineItems = response.line_items.toMutableList().apply {
                        add(
                            LineItem(
                                id = product.id,
                                name = product.title,
                                price = product.variants[0].price,
                                sku = product.image,
                                title = product.product_type
                            )
                        )
                    }
                    val updatedDraftOrder = response.copy(line_items = newLineItems)

                    repository.updateDraftOrder(id, DraftOrderResponse(updatedDraftOrder))
                        .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
                        .collect { data -> _favoriteProduct.value = ApiState.Success(data) }
                }

        }
    }
}

