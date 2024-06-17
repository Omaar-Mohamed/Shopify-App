package com.example.shopify_app.features.signup.data.model.DarftOrderRequest

data class DraftOrder(
    val customer: Customer,
    val line_items: List<LineItem> = listOf(LineItem(null, quantity = 1, properties = listOf()))
)