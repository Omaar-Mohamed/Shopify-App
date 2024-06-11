package com.example.shopify_app.core.networking


import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import kotlinx.coroutines.flow.Flow

interface AppRemoteDataSourse {
    suspend fun signUpCustomer(signupRequest: SignupRequest): CustomerRespones
    suspend fun updateCustomer(id: String, updateCustomer: UpdateCustomer): CustomerRespones
    suspend fun createDraftOrder(draftOderRequest: DraftOderRequest): DraftOrderResponse

    suspend fun getPriceRules(): Flow<PriceRulesResponse>
    suspend fun getBrandsRules(): Flow<SmartCollectionResponse>

    suspend fun getProducts(): Flow<ProductsResponse>


}