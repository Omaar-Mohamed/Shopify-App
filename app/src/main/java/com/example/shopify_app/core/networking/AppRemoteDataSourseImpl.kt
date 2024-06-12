package com.example.shopify_app.core.networking

import com.example.shopify_app.core.networking.Auth.AuthServices
import com.example.shopify_app.core.networking.DraftOrder.DraftOrderServices
import com.example.shopify_app.core.networking.RetrofitHelper.retrofitInstance

import com.example.shopify_app.core.networking.RetrofitManager.retrofitManagerInstance
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AppRemoteDataSourseImpl : AppRemoteDataSourse {
    override suspend fun signUpCustomer(
       signupRequest: SignupRequest
    ): CustomerRespones {
        return retrofitManagerInstance.create(AuthServices::class.java)
            .signUpCustomer(signupRequest)
    }

    override suspend fun updateCustomer(id: String, updateCustomer: UpdateCustomer): CustomerRespones {
        return retrofitManagerInstance.create(AuthServices::class.java)
            .updateCustomer(id,updateCustomer)
    }

    override suspend fun getCustomer(email: String): Flow<LoginCustomer> = flow {
        val response = retrofitManagerInstance.create(AuthServices::class.java)
            .getCustomer(email)
        emit(response)
    }

    override suspend fun createDraftOrder(draftOderRequest: DraftOderRequest): DraftOrderResponse {
        return retrofitManagerInstance.create(DraftOrderServices::class.java)
            .postDraftOrder(draftOderRequest)
    }

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