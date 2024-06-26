package com.example.shopify_app.features.myOrders.data.model

data class DefaultAddress(
    val address1: String,
    val address2: String,
    val city: String,
    val company: String,
    val country: String,
    val country_code: String,
    val country_name: String,
    val customer_id: Long,
    val default: Boolean,
    val first_name: String,
    val id: Long,
    val last_name: Any,
    val name: String,
    val phone: String,
    val province: String,
    val province_code: Any,
    val zip: String
)