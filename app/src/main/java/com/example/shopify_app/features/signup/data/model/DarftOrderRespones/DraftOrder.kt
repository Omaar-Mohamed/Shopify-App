package com.example.shopify_app.features.signup.data.model.DarftOrderRespones

data class DraftOrder(
    val customer: Customer,
    val id: Long,
    val line_items: List<LineItem>,

)