package com.example.shopify_app.core.networking.DraftOrder

import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
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

    @PUT("admin/api/2024-04/draft_orders/{id}.json")
    suspend fun updateDraftOrder(
        @Path("id") id: String,
        @Body draftOrder: DraftOrderResponse
    ): DraftOrderResponse

    @GET("admin/api/2024-04/draft_orders/{id}.json")
    suspend fun getDraftOrder(
        @Path("id") id: String
    ): DraftOrderResponse
}
