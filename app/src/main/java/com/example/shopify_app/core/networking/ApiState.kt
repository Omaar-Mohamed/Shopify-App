package com.example.shopify_app.core.networking

import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse

sealed class ApiState {
    object Loading : ApiState()
    data class Success(val data: PriceRulesResponse) : ApiState()
    data class Failure(val error: Throwable) : ApiState()
}