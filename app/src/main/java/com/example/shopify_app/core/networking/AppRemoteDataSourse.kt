package com.example.shopify_app.core.networking


import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import com.example.shopify_app.features.signup.data.model.CustomerRequest.SignupRequest
import com.example.shopify_app.features.signup.data.model.CustomerRespones.CustomerRespones
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.UpdateCustomer.UpdateCustomer
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderRequest
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Path


interface AppRemoteDataSourse {
    suspend fun signUpCustomer(signupRequest: SignupRequest): CustomerRespones
    suspend fun updateCustomer(id: String, updateCustomer: UpdateCustomer): CustomerRespones
    suspend fun getCustomer(email: String): Flow<LoginCustomer>
    suspend fun getCustomerByEmail(email: String): LoginCustomer
    suspend fun createDraftOrder(draftOderRequest: DraftOderRequest): DraftOrderResponse

    suspend fun getPriceRules(): Flow<PriceRulesResponse>
    suspend fun getBrandsRules(): Flow<SmartCollectionResponse>

    suspend fun getProducts(): Flow<ProductsResponse>
    suspend fun getCustomCollections(): Flow<CustomCategoriesResponse>

    suspend fun getProductsById(collectionId: String): Flow<ProductsByIdResponse>
    suspend fun getProductsDetails(id: String): Flow<ProductDetailResponse>

    suspend fun getAddresses(customerId : String) : Flow<AddressResponse>

    suspend fun addAddress(customerId: String, address: PostAddressRequest) : Flow<PostAddressResponse>

    suspend fun updateAddress(customerId: String, addressId : String, address: PostAddressRequest) : Flow<PostAddressResponse>

    suspend fun makeAddressDefault(customerId: String,addressId: String) : Flow<PostAddressResponse>
    suspend fun deleteAddress(customerId: String, addressId : String) : Flow<PostAddressResponse>
    suspend fun getDraftOrder(id: String) : Flow<DraftOrderResponse>
    suspend fun updateDraftOrder(id : String, newDraftOrder: DraftOrder) : Flow<DraftOrderResponse>

    suspend fun getOrders(customerId: Long?): Flow<OrdersResponse>

    suspend fun getOrdersDetails(orderId: Long): Flow<OrderDetailsResponse>
    suspend fun getConversionRate(base : String , to : String) : Flow<ConversionResponse>

    suspend fun createOrder(orderRequest: OrderRequest): Flow<OrderDetailsResponse>
}