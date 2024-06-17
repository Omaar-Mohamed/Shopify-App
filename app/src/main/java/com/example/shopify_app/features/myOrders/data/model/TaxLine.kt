package com.example.shopify_app.features.myOrders.data.model

data class TaxLine(
    val channel_liable: Boolean,
    val price: String,
    val price_set: PriceSetX,
    val rate: Double,
    val title: String
)