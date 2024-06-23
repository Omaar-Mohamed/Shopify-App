package com.example.shopify_app.features.payment.data.repo

import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface PaymentRepo {
    suspend fun createCheckout(checkoutRequest: CheckoutRequest) : Flow<CheckoutResponse>
    suspend fun completeDraft(id : String) : Flow<DraftOrderResponse>
}