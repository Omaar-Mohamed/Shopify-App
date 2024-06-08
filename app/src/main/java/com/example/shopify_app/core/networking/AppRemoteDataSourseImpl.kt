package com.example.shopify_app.core.networking

import com.example.shopify_app.core.networking.RetrofitHelper.retrofitInstance
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AppRemoteDataSourseImpl : AppRemoteDataSourse {
    override suspend fun getPriceRules(): Flow<PriceRulesResponse> = flow {
        val response = retrofitInstance.create(NetworkServices::class.java).getPriceRules()
        emit(response)

    }
}