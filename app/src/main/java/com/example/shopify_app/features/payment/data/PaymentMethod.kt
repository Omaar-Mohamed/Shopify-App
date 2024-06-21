package com.example.shopify_app.features.payment.data

import com.google.gson.annotations.SerializedName

enum class PaymentMethod {
    PAYMENT_CARDS, CASH_ON_DELIVERY
}

data class StripeEphemeralKeyResponse(
    @SerializedName("id") val id: String,
    @SerializedName("object") val objectType: String,
    @SerializedName("created") val created: Long,
    @SerializedName("expires") val expires: Long,
    @SerializedName("livemode") val livemode: Boolean,
    @SerializedName("associated_objects") val associatedObjects: List<AssociatedObject>
)

data class AssociatedObject(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String
)