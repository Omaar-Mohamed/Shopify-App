package com.example.shopify_app.features.ProductDetails.data.repo

import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface ProductsDetailsRepo {
    suspend fun getProductsDetails(id: String): Flow<ProductDetailResponse>
    suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse>
    suspend fun updateDraftOrder( id: String, newDraftOrder: DraftOrder) : Flow<DraftOrderResponse>
}