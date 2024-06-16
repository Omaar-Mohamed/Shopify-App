package com.example.shopify_app.features.wishList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.ProductDetails.data.model.Product
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.features.wishList.data.WishListRope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class WishListViewModel(private val repository: WishListRope) : ViewModel(){

    private val _favoriteProduct: MutableStateFlow<ApiState<DraftOrderResponse>> = MutableStateFlow(ApiState.Loading)
    val favoriteProduct: StateFlow<ApiState<DraftOrderResponse>> = _favoriteProduct

    fun getFavProducts(id : String){
        viewModelScope.launch {
            repository.getDraftOrder(id)
                .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
                .collect { data -> _favoriteProduct.value = ApiState.Success(data) }
        }
    }

    fun insertFavProduct(id : String,product: Product){
        viewModelScope.launch{
            repository.getDraftOrder(id)
                .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
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

                    repository.updateDraftOrder(id,DraftOrderResponse(updatedDraftOrder))
                        .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
                        .collect { data -> _favoriteProduct.value = ApiState.Success(data) }
                }

        }
    }

    fun deleteFavProduct(id : String, lineItem: LineItem){
        viewModelScope.launch{
            repository.getDraftOrder(id)
                .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
                .collect { data ->
                    val response = data.draft_order
                    val newLineItems = response.line_items.toMutableList().apply {
                        remove(lineItem)
                    }

                    val updatedDraftOrder = response.copy(line_items = newLineItems)
                    repository.updateDraftOrder(id,DraftOrderResponse(updatedDraftOrder))
                        .catch { e -> _favoriteProduct.value = ApiState.Failure(e) }
                        .collect { data -> _favoriteProduct.value = ApiState.Success(data) }
                }
        }
    }

}
