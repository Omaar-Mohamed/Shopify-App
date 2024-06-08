package com.example.shopify_app.core.networking

import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import retrofit2.http.GET

interface NetworkServices {
    @GET("admin/api/2024-04/price_rules.json")
    suspend fun getPriceRules(): PriceRulesResponse

    @GET("admin/api/2024-04/smart_collections.json")
    suspend fun getSmartCollections(): SmartCollectionResponse

    @GET("admin/api/2024-04/products.json")
       suspend fun getProducts(): ProductsResponse
}