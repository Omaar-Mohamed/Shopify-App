package com.example.shopify_app.features.categories.data.repo

import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import kotlinx.coroutines.flow.Flow

interface CategoriesRepo {

    suspend fun getCategories(): Flow<CustomCategoriesResponse>
}