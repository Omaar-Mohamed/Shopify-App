package com.example.shopify_app.features.myOrders.data.model.orderRequest

import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem

data class OrderRequest(
    val order: OrderReq
)

data class OrderReq(
    val line_items: List<LineItemRequest>,
    val email: String,
    val send_receipt: Boolean
)

data class LineItemRequest(
    val variant_id: Long,
    val quantity: Int
)
