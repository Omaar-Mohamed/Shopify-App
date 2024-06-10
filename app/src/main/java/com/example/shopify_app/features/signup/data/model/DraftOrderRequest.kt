package com.example.shopify_app.features.signup.data.model

import android.os.Parcelable

data class DraftOrderRequest(
    val customer: Customer,
    val line_items: List<LineItem> = listOf(LineItem(null, quantity = 1)),
    )

data class LineItem(
    val variant_id: Long?,
    val price: String? = "1",
    val quantity: Int?,
    val title: String? = "dummy"
)
