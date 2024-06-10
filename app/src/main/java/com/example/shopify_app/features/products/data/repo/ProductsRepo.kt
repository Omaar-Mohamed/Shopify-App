package com.example.shopify_app.features.products.data.repo

import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import kotlinx.coroutines.flow.Flow

interface ProductsRepo {
    suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse>
}