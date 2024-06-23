package com.example.shopify_app.features.personal_details.data.repo

import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import kotlinx.coroutines.flow.Flow

interface PersonalRepo {
    suspend fun getAddresses(customerId : String) : Flow<AddressResponse>

    suspend fun addAddresses(customerId: String, address:PostAddressRequest) : Flow<PostAddressResponse>

    suspend fun updateAddress(customerId: String, addressId : String, address: PostAddressRequest) : Flow<PostAddressResponse>

    suspend fun makeAddressDefault(customerId: String,addressId: String) :Flow<PostAddressResponse>

    suspend fun deleteAddress(customerId: String, addressId : String) : Flow<PostAddressResponse>

}