package com.example.shopify_app.features.home.data.models.ProductsResponse

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
)