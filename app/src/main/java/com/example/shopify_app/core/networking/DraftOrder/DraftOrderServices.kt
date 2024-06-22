package com.example.shopify_app.core.networking.DraftOrder

import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DraftOrderServices {
    @POST("admin/api/2024-04/draft_orders.json")
    suspend fun postDraftOrder(
        @Body body: DraftOderRequest,
    ) : DraftOrderResponse


    @GET("admin/api/2024-04/draft_orders/{draftOrderId}.json")
    suspend fun getDraftOrder(@Path("draftOrderId") id : String) : DraftOrderResponse
    @GET("admin/api/2024-04/draft_orders.json")
    suspend fun getAllDraftOrders()
    @PUT("admin/api/2024-04/draft_orders/{draftOrderId}.json")
    suspend fun updateDraftOrder(@Path("draftOrderId") id : String,@Body newDraftOrder : DraftOrderResponse) : DraftOrderResponse

    @PUT("/admin/api/2024-04/draft_orders/{draftOrderId}/complete.json")
    suspend fun completeDraftOrder(@Path("draftOrderId") id : String) : DraftOrderResponse

}
