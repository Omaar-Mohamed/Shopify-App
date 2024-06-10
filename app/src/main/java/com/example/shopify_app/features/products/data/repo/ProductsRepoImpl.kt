package com.example.shopify_app.features.products.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import kotlinx.coroutines.flow.Flow

class ProductsRepoImpl private constructor(
    private val appRemoteDataSourse: AppRemoteDataSourse
) : ProductsRepo {

    companion object {
        private var instance: ProductsRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): ProductsRepoImpl {
            if (instance == null) {
                instance = ProductsRepoImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }

    override suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse> {
        return appRemoteDataSourse.getProductsById(collectionId)
    }


}