package com.example.shopify_app.features.signup.data.model.DarftOrderRequest

data class LineItem(
    val variant_id: Long?,
    val price: String? = "1",
    val quantity: Int?,
    val title: String? = "dummy"
)