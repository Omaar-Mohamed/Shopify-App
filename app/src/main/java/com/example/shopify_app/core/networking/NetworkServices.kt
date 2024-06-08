package com.example.shopify_app.core.networking

import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import retrofit2.http.GET

interface NetworkServices {
    @GET("admin/api/2021-04/price_rules.json")
    suspend fun getPriceRules(): PriceRulesResponse
}