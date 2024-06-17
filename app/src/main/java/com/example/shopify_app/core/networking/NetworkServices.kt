package com.example.shopify_app.core.networking

import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import com.example.shopify_app.features.products.data.model.ProductsByIdResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkServices {
    @GET("admin/api/2024-04/price_rules.json")
    suspend fun getPriceRules(): PriceRulesResponse

    @GET("admin/api/2024-04/smart_collections.json")
    suspend fun getSmartCollections(): SmartCollectionResponse

    @GET("admin/api/2024-04/products.json")
       suspend fun getProducts(): ProductsResponse

    @GET("admin/api/2024-04/custom_collections.json")
    suspend fun getCustomCollections(): CustomCategoriesResponse

    @GET("admin/api/2024-04/collections/{collectionId}/products.json")
    suspend fun getProductsById(
        @Path("collectionId") collectionId: String
    ): ProductsByIdResponse

    @GET("admin/api/2024-04/customers/{customerId}/addresses.json")
    suspend fun getAddresses(
        @Path("customerId") customerId : String
    ) : AddressResponse

    @POST("admin/api/2024-04/customers/{customerId}/addresses.json")
    suspend fun postAddress(@Path("customerId") customerId: String, @Body address : PostAddressRequest) : PostAddressResponse

    @PUT("/admin/api/2023-10/customers/{customerId}/addresses/{addressId}.json")
    suspend fun updateAddress(@Path("customerId") customerId: String,@Path("addressId") addressId : String,@Body address: PostAddressRequest) : PostAddressResponse

    @DELETE("/admin/api/2023-10/customers/{customerId}/addresses/{addressId}.json")
    suspend fun deleteAddress(@Path("customerId") customerId: String,@Path("addressId") addressId : String) : PostAddressResponse
    @GET("admin/api/2024-04/products/{id}.json")
    suspend fun getProductsDetails(
        @Path("id") id: String
    ): ProductDetailResponse

    @GET("admin/api/2024-04/orders.json?status=any")
    suspend fun getOrders(@Query("customer_id") customerId: Long?): OrdersResponse

    @GET("admin/api/2024-04/orders/{orderId}.json")
    suspend fun getOrderDetails(@Path("orderId") orderId: Long): OrderDetailsResponse


}