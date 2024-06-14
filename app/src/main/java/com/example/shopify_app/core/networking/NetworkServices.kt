package com.example.shopify_app.core.networking

import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkServices {
    @GET("admin/api/2024-04/price_rules.json")
    suspend fun getPriceRules(): PriceRulesResponse

    @GET("admin/api/2024-04/smart_collections.json")
    suspend fun getSmartCollections(): SmartCollectionResponse

    @GET("admin/api/2024-04/products.json")
       suspend fun getProducts(): ProductsResponse

    @GET("admin/api/2024-04/custom_collections.json")
    suspend fun getCustomCollections(): CustomCategoriesResponse

    @GET("admin/api/2024-04/collections/{collectionId}/products.json")
    suspend fun getProductsById(
        @Path("collectionId") collectionId: String
    ): ProductsByIdResponse

    @GET("admin/api/2024-04/products/{id}.json")
    suspend fun getProductsDetails(
        @Path("id") id: String
    ): ProductDetailResponse


}