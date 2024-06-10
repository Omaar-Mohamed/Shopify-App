package com.example.shopify_app.features.signup.data.model

import com.fasterxml.jackson.annotation.JsonProperty

data class DraftOrder(
    val id: Long,
    val customer: Customer,
    @JsonProperty("line_items")
    val lineItems: List<LineItem>
)