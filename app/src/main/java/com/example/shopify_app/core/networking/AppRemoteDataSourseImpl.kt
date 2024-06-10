package com.example.shopify_app.core.networking

import com.example.shopify_app.core.networking.RetrofitHelper.retrofitInstance
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AppRemoteDataSourseImpl : AppRemoteDataSourse {
    override suspend fun getPriceRules(): Flow<PriceRulesResponse> = flow {
        val response = retrofitInstance.create(NetworkServices::class.java).getPriceRules()
        emit(response)

    }

    override suspend fun getBrandsRules(): Flow<SmartCollectionResponse> = flow {
        val response = retrofitInstance.create(NetworkServices::class.java).getSmartCollections()
        emit(response)


    }

    override suspend fun getProducts(): Flow<ProductsResponse> = flow {
            val response = retrofitInstance.create(NetworkServices::class.java).getProducts()
        emit(response)
    }

    override suspend fun getCustomCollections(): Flow<CustomCategoriesResponse> = flow {
        val response = retrofitInstance.create(NetworkServices::class.java).getCustomCollections()
        emit(response)

    }

    override suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse> = flow {
        val response = retrofitInstance.create(NetworkServices::class.java).getProductsById(collectionId)
        emit(response)
    }

}