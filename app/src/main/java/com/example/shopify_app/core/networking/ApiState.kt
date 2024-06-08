package com.example.shopify_app.core.networking

import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Failure(val error: Throwable) : ApiState<Nothing>()
}
