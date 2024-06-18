package com.example.shopify_app.features.categories.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import kotlinx.coroutines.flow.Flow

class CategoriesRepoImpl private constructor(
    private val appRemoteDataSourse: AppRemoteDataSourse
)
    : CategoriesRepo {
        companion object {
            private var instance: CategoriesRepoImpl? = null
            fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): CategoriesRepoImpl {
                if (instance == null) {
                    instance = CategoriesRepoImpl(appRemoteDataSourse)
                }
                return instance!!
            }
        }

    override suspend fun getCategories(): Flow<CustomCategoriesResponse> {
        return appRemoteDataSourse.getCustomCollections()
    }
}