package com.example.shopify_app.core.networking

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
import retrofit2.http.Body
import retrofit2.http.Path

interface AppRemoteDataSourse {
    suspend fun getPriceRules(): Flow<PriceRulesResponse>
    suspend fun getBrandsRules(): Flow<SmartCollectionResponse>

    suspend fun getProducts(): Flow<ProductsResponse>
    suspend fun getCustomCollections(): Flow<CustomCategoriesResponse>

    suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse>

    suspend fun getAddresses(customerId : String) : Flow<AddressResponse>

    suspend fun addAddress(customerId: String, address: PostAddressRequest) : Flow<PostAddressResponse>

    suspend fun updateAddress(customerId: String, addressId : String, address: PostAddressRequest) : Flow<PostAddressResponse>
    suspend fun deleteAddress(customerId: String, addressId : String) : Flow<PostAddressResponse>
}