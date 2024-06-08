package com.example.shopify_app.features.home.data.repo

import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    suspend fun getPriceRules(): Flow<PriceRulesResponse>

    suspend fun getSmartCollections(): Flow<SmartCollectionResponse>
}