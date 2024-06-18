package com.example.shopify_app.features.myOrders.data.model

data class Customer(
    val admin_graphql_api_id: String,
    val created_at: String,
    val currency: String,
    val default_address: DefaultAddress,
    val email: String,
    val email_marketing_consent: EmailMarketingConsent,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val multipass_identifier: String,
    val note: String,
    val phone: Any,
    val sms_marketing_consent: Any,
    val state: String,
    val tags: String,
    val tax_exempt: Boolean,
    val tax_exemptions: List<Any>,
    val updated_at: String,
    val verified_email: Boolean
)