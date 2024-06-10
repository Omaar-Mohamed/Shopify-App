package com.example.shopify_app.core.networking.DraftOrder

import com.example.shopify_app.features.signup.data.model.DraftOrder
import com.example.shopify_app.features.signup.data.model.DraftOrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DraftOrderServices {
    @POST("draft_orders.json")
    suspend fun postDraftOrder(
        @Body body: DraftOrderRequest,
        @Header("X-Shopify-Access-Token") passwordToken: String = "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"
    ) :Response<DraftOrder>


//@GET("draft_orders/{id}.json")
}