package com.example.shopify_app.core.models

data class CheckoutResponse(
    val id : String,
    val url: String
)

data class AutomaticTax(
    val enabled: Boolean,
    val liability: Any,
    val status: Any
)

data class CustomText(
    val shipping_address: Any,
    val submit: Any
)

data class InvoiceCreation(
    val enabled: Boolean,
    val invoice_data: InvoiceData
)

class MetadataX

class PaymentMethodOptions

data class PhoneNumberCollection(
    val enabled: Boolean
)

data class TotalDetails(
    val amount_discount: Int,
    val amount_shipping: Int,
    val amount_tax: Int
)

data class InvoiceData(
    val account_tax_ids: Any,
    val custom_fields: Any,
    val description: Any,
    val footer: Any,
    val issuer: Any,
    val metadata: MetadataX,
    val rendering_options: Any
)

data class CheckoutRequest(
    val success_url: String,
    val cancel_url: String,
    val mode: String,
    val line_items: List<StripeLineItem>,
    val customer_email : String
)

data class StripeLineItem(
    val price_data : PriceData,
    val quantity: Int
)

data class PriceData(
    val currency: String,
    val unit_amount: Int,
    val product_data : ProductData
)

data class ProductData(
    val name : String,
    val description : String
)