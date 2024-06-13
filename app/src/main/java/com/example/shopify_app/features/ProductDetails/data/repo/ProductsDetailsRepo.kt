package com.example.shopify_app.features.ProductDetails.data.repo

import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import kotlinx.coroutines.flow.Flow

interface ProductsDetailsRepo {
    suspend fun getProductsDetails(id: String): Flow<ProductDetailResponse>
}