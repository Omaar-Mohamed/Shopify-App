package com.example.shopify_app.features.signup.data.model.DarftOrderRespones

data class LineItem(
    val admin_graphql_api_id: String,
    val applied_discount: Any,
    val custom: Boolean,
    val fulfillment_service: String,
    val gift_card: Boolean,
    val grams: Int,
    val id: Long,
    val name: String,
    val price: String,
    val product_id: Any,
    val properties: List<Any>,
    val quantity: Int,
    val requires_shipping: Boolean,
    val sku: Any,
    val taxable: Boolean,
    val title: String,
    val variant_id: Any,
    val variant_title: Any,
    val vendor: Any
)