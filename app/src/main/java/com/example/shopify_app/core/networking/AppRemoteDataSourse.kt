package com.example.shopify_app.core.networking

import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import kotlinx.coroutines.flow.Flow

interface AppRemoteDataSourse {
    suspend fun getPriceRules(): Flow<PriceRulesResponse>
}