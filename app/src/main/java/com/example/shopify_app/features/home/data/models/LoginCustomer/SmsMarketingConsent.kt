package com.example.shopify_app.features.home.data.models.LoginCustomer

data class SmsMarketingConsent(
    val consent_collected_from: String,
    val consent_updated_at: String,
    val opt_in_level: String,
    val state: String
)