package com.example.shopify_app.core.models

enum class Currency {
    USD,EGP
}

data class ConversionResponse(
    val amount: Int,
    val base_currency_code: String,
    val base_currency_name: String,
    val rates: Rates,
    val status: String,
    val updated_date: String
)

data class Rates(
    val USD: RateDetails
)

data class RateDetails(
    val currency_name: String,
    val rate: String,
    val rate_for_amount: String
)