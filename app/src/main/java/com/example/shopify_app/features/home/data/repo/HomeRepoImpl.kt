package com.example.shopify_app.features.home.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import kotlinx.coroutines.flow.Flow

class HomeRepoImpl private constructor(
    private val appRemoteDataSourse: AppRemoteDataSourse
) : HomeRepo{

    companion object {
        private var instance: HomeRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): HomeRepoImpl {
            if (instance == null) {
                instance = HomeRepoImpl(appRemoteDataSourse)
            }
            return instance!!
        }
    }

    override suspend fun getCustomer(email: String): Flow<LoginCustomer> {
        return appRemoteDataSourse.getCustomer(email)
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse> {
        return appRemoteDataSourse.getPriceRules()
    }

    override suspend fun getSmartCollections(): Flow<SmartCollectionResponse> {
        return appRemoteDataSourse.getBrandsRules()
    }

    override suspend fun getProducts(): Flow<ProductsResponse> {
        return appRemoteDataSourse.getProducts()
    }
}