package com.example.shopify_app.features.payment.data.repo

import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

class PaymentRepoImpl(
    val remoteDataSourse: AppRemoteDataSourse
) : PaymentRepo {
    override suspend fun createCheckout(checkoutRequest: CheckoutRequest): Flow<CheckoutResponse> {
        return remoteDataSourse.createCheckout(checkoutRequest)
    }

}