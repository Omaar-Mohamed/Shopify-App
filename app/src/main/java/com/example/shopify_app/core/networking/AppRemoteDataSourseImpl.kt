package com.example.shopify_app.core.networking

import android.util.Log
import com.example.shopify_app.core.networking.RetrofitHelper.retrofitInstance
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log

object AppRemoteDataSourseImpl : AppRemoteDataSourse {
    private val services: NetworkServices = retrofitInstance.create(NetworkServices::class.java)
    override suspend fun getPriceRules(): Flow<PriceRulesResponse> = flow {
        val response = services.getPriceRules()
        emit(response)

    }

    override suspend fun getBrandsRules(): Flow<SmartCollectionResponse> = flow {
        val response = services.getSmartCollections()
        emit(response)


    }

    override suspend fun getProducts(): Flow<ProductsResponse> = flow {
        val response = services.getProducts()
        emit(response)
    }

    override suspend fun getCustomCollections(): Flow<CustomCategoriesResponse> = flow {
        val response = services.getCustomCollections()
        emit(response)

    }

    override suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse> = flow {
        val response = services.getProductsById(collectionId)
        emit(response)
    }

    override suspend fun getAddresses(customerId: String): Flow<AddressResponse> = flow{
        val response = services.getAddresses(customerId)
        Log.i("TAG", "getAddresses: ${response.addresses}")
        emit(response)
    }

    override suspend fun addAddress(customerId: String, address: PostAddressRequest): Flow<PostAddressResponse> = flow {
        val response = services.postAddress(customerId,address)
        emit(response)
    }

    override suspend fun updateAddress(
        customerId: String,
        addressId: String,
        address: PostAddressRequest
    ): Flow<PostAddressResponse>  = flow{
        val response = services.updateAddress(customerId,addressId,address)
        emit(response)
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> = flow {
        val response = services.deleteAddress(customerId, addressId)
        emit(response)
    }


}