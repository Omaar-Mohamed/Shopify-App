package com.example.shopify_app.core.networking

import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import kotlinx.coroutines.flow.Flow

interface AppRemoteDataSourse {
    suspend fun getPriceRules(): Flow<PriceRulesResponse>
    suspend fun getBrandsRules(): Flow<SmartCollectionResponse>

    suspend fun getProducts(): Flow<ProductsResponse>
    suspend fun getCustomCollections(): Flow<CustomCategoriesResponse>


}