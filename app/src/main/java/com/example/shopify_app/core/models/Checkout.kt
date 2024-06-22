package com.example.shopify_app.core.models

data class CheckoutResponse(
    val after_expiration: Any,
    val allow_promotion_codes: Any,
    val amount_subtotal: Int,
    val amount_total: Int,
    val automatic_tax: AutomaticTax,
    val billing_address_collection: Any,
    val cancel_url: Any,
    val client_reference_id: Any,
    val consent: Any,
    val consent_collection: Any,
    val created: Int,
    val currency: String,
    val custom_fields: List<Any>,
    val custom_text: CustomText,
    val customer: Any,
    val customer_creation: String,
    val customer_details: Any,
    val customer_email: Any,
    val expires_at: Int,
    val id: String,
    val invoice: Any,
    val invoice_creation: InvoiceCreation,
    val livemode: Boolean,
    val locale: Any,
    val metadata: MetadataX,
    val mode: String,
    val `object`: String,
    val payment_intent: Any,
    val payment_link: Any,
    val payment_method_collection: String,
    val payment_method_options: PaymentMethodOptions,
    val payment_method_types: List<String>,
    val payment_status: String,
    val phone_number_collection: PhoneNumberCollection,
    val recovered_from: Any,
    val setup_intent: Any,
    val shipping_address_collection: Any,
    val shipping_cost: Any,
    val shipping_details: Any,
    val shipping_options: List<Any>,
    val status: String,
    val submit_type: Any,
    val subscription: Any,
    val success_url: String,
    val total_details: TotalDetails,
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