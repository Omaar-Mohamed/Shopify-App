package com.example.shopify_app.core.networking

import android.util.Log
import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.networking.Auth.AuthServices
import com.example.shopify_app.core.networking.DraftOrder.DraftOrderServices
import com.example.shopify_app.core.networking.RetrofitHelper.retrofitCurrency
import com.example.shopify_app.core.networking.RetrofitHelper.retrofitInstance
import com.example.shopify_app.core.networking.RetrofitHelper.stripeRetrofit
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
import kotlinx.coroutines.flow.flow
import kotlin.math.log

object AppRemoteDataSourseImpl : AppRemoteDataSourse {
    private val services: NetworkServices = retrofitInstance.create(NetworkServices::class.java)
    override suspend fun signUpCustomer(
       signupRequest: SignupRequest
    ): CustomerRespones {
        return retrofitInstance.create(AuthServices::class.java)
            .signUpCustomer(signupRequest)
    }

    override suspend fun updateCustomer(id: String, updateCustomer: UpdateCustomer): CustomerRespones {
        return retrofitInstance.create(AuthServices::class.java)
            .updateCustomer(id,updateCustomer)
    }

    override suspend fun getCustomer(email: String): Flow<LoginCustomer> = flow {
        val response = retrofitInstance.create(AuthServices::class.java)
            .getCustomer(email)
        emit(response)
    }

    override suspend fun createDraftOrder(draftOderRequest: DraftOderRequest): DraftOrderResponse {
        return retrofitInstance.create(DraftOrderServices::class.java)
            .postDraftOrder(draftOderRequest)
    }

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

    override suspend fun makeAddressDefault(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> = flow{
        val response = services.makeAddressDefault(customerId,addressId)
        emit(response)
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String
    ): Flow<PostAddressResponse> = flow {
        val response = services.deleteAddress(customerId, addressId)
        emit(response)
    }

    override suspend fun getOrders(customerId: Long?): Flow<OrdersResponse> = flow{
        val response = services.getOrders(customerId)
        emit(response)
    }

    override suspend fun getOrdersDetails(orderId: Long): Flow<OrderDetailsResponse> = flow{
        val response = services.getOrderDetails(orderId)
        emit(response)

    }

    override suspend fun createOrder(orderRequest: OrderRequest): Flow<OrderDetailsResponse> = flow {
        val response = services.createOrder(orderRequest)
        emit(response)
    }


    override suspend fun getProductsDetails(id: String): Flow<ProductDetailResponse> = flow{
        val response = retrofitInstance.create(NetworkServices::class.java).getProductsDetails(id)
        emit(response)
    }

    override suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse> = flow {
        val response = retrofitInstance.create(DraftOrderServices::class.java).getDraftOrder(id)
        emit(response)
    }

    override suspend fun updateDraftOrder(id: String, newDraftOrder: DraftOrder): Flow<DraftOrderResponse> = flow {
        val response = retrofitInstance.create(DraftOrderServices::class.java).updateDraftOrder(id,DraftOrderResponse(newDraftOrder))
        emit(response)
    }

    override suspend fun completeDraftOrder(id: String): Flow<DraftOrderResponse> =flow{
        val response = retrofitInstance.create(DraftOrderServices::class.java).completeDraftOrder(id)
        emit(response)
    }

    override suspend fun getConversionRate(base: String, to: String): Flow<ConversionResponse> = flow{
        val response = retrofitCurrency.create(CurrencyServices::class.java).getConversionRate(base,to)
        emit(response)
    }

    private val stripeService = RetrofitHelper.stripeRetrofit.create(StripeServices::class.java)

    override suspend fun createCheckout(checkoutRequest: CheckoutRequest): Flow<CheckoutResponse> = flow {
        val lineItemsMap = checkoutRequest.line_items.flatMapIndexed { index, item ->
            listOf(
                "line_items[$index][price_data][unit_amount]" to item.price_data.unit_amount.toString(),
                "line_items[$index][price_data][currency]" to item.price_data.currency,
                "line_items[$index][price_data][product_data][name]" to item.price_data.product_data.name,
                "line_items[$index][quantity]" to item.quantity.toString()
            )
        }.toMap()

        val response = stripeService.createSession(
            successUrl = checkoutRequest.success_url,
            cancelUrl = checkoutRequest.cancel_url,
            mode = checkoutRequest.mode,
            lineItems = lineItemsMap,
            email = checkoutRequest.customer_email
        )
        emit(response)
    }

}