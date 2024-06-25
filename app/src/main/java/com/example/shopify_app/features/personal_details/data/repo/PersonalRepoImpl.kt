package com.example.shopify_app.features.personal_details.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import com.example.shopify_app.features.products.data.repo.ProductsRepoImpl
import kotlinx.coroutines.flow.Flow

class PersonalRepoImpl(
    private val appRemoteDataSource: AppRemoteDataSourse
) : PersonalRepo {
    companion object {
        private var instance: PersonalRepoImpl? = null
        fun getInstance(appRemoteDataSource: AppRemoteDataSourse): PersonalRepoImpl {
            if (instance == null) {
                instance = PersonalRepoImpl(appRemoteDataSource)
            }
            return instance!!
        }
    }

    override suspend fun getAddresses(customerId: String): Flow<AddressResponse> {
         return appRemoteDataSource.getAddresses(customerId)
    }

    override suspend fun addAddresses(customerId: String, address: PostAddressRequest): Flow<PostAddressResponse> {
        return appRemoteDataSource.addAddress(customerId,address)
    }

    override suspend fun updateAddress(
        customerId: String,
        addressId: String,
        address: PostAddressRequest
    ): Flow<PostAddressResponse> {
        return appRemoteDataSource.updateAddress(customerId, addressId, address)
    }

    override suspend fun makeAddressDefault(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> {
        return appRemoteDataSource.makeAddressDefault(customerId,addressId)
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> {
        return appRemoteDataSource.deleteAddress(customerId, addressId)
    }

}