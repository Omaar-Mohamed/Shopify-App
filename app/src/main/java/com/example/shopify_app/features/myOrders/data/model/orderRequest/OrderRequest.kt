package com.example.shopify_app.features.myOrders.data.model.orderRequest

import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Property
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.AppliedDiscount
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem

data class OrderRequest(
    val order: OrderReq
)

data class OrderReq(
    val line_items: List<LineItemRequest>,
    val email: String,
    val send_receipt: Boolean,
    val discount_codes : List<DiscountCode>
)

data class DiscountCode(
    var code : String,
    var amount : String,
    var type : String
)

data class LineItemRequest(
    val variant_id: Long,
    val quantity: Int,
    val properties : List<Property>
)
