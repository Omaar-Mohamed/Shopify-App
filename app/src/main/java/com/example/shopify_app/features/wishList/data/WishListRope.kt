package com.example.shopify_app.features.wishList.data

import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.DraftOderRequest
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface WishListRope {
    suspend fun updateDraftOrder(id: String, draftOrder: DraftOrderResponse): Flow<DraftOrderResponse>
    suspend fun getDraftOrder(id: String): Flow<DraftOrderResponse>
}