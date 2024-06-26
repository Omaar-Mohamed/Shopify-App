package com.example.shopify_app.features.personal_details.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import kotlinx.coroutines.flow.Flow

class PersonalRepoImpl(
    private val appRemoteDataSource: AppRemoteDataSourse
) : PersonalRepo {

    companion object {
        @Volatile
        private var instance: PersonalRepoImpl? = null

        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse): PersonalRepoImpl {
            return instance ?: synchronized(this) {
                instance ?: PersonalRepoImpl(appRemoteDataSourse).also { instance = it }
            }
        }
    }

    override suspend fun getAddresses(customerId: String): Flow<AddressResponse> {
        return appRemoteDataSource.getAddresses(customerId)
    }

    override suspend fun addAddresses(customerId: String, address: PostAddressRequest): Flow<PostAddressResponse> {
        return appRemoteDataSource.addAddress(customerId, address)
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
        return appRemoteDataSource.makeAddressDefault(customerId, addressId)
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> {
        return appRemoteDataSource.deleteAddress(customerId, addressId)
    }
}
