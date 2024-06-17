package com.example.shopify_app.features.home.data.repo

import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    suspend fun getCustomer(email: String): Flow<LoginCustomer>
    suspend fun getPriceRules(): Flow<PriceRulesResponse>

    suspend fun getSmartCollections(): Flow<SmartCollectionResponse>

    suspend fun getProducts(): Flow<ProductsResponse>
    suspend fun updateDraftOrder(id: String, draftOrder: DraftOrderResponse): Flow<DraftOrderResponse>
    suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse>
}