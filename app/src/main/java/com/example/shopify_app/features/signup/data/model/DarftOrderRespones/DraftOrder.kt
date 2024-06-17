package com.example.shopify_app.features.signup.data.model.DarftOrderRespones

//data class DraftOrder(
//    val customer: Customer,
//    val id: Long,
//    val line_items: List<LineItem>,
//
//)

data class DraftOrder(
    val applied_discount: AppliedDiscount? = null,
    val billing_address: BillingAddress?,
    val completed_at: String?,
    val created_at: String?,
    val currency: String?,
    val customer: Customer?,
    val email: String?,
    val id: Long,
    val invoice_sent_at: String?,
    val invoice_url: String?,
    val line_items: List<LineItem>,
    val name: String?,
    val note: Any?,
    val note_attributes: List<NoteAttribute>?,
    val order_id: OrderId?,
    val payment_terms: PaymentTerms?,
    val shipping_address: ShippingAddress?,
    val shipping_line: ShippingLine?,
    val source_name: String?,
    val status: String?,
    val subtotal_price: String?,
    val tags: String?,
    val tax_exempt: Boolean?,
    val tax_exemptions: List<String>?,
    val tax_lines: List<TaxLineX>?,
    val taxes_included: Boolean?,
    val total_price: String?,
    val total_tax: String?,
    val updated_at: String?
)

data class AppliedDiscount(
    val amount: String,
    val description: String,
    val title: String,
    val value: String,
    val value_type: String
)

data class BillingAddress(
    val address1: String,
    val address2: String,
    val city: String,
    val company: Any,
    val country: String,
    val country_code: Any,
    val default: Boolean,
    val first_name: Any,
    val id: Int,
    val last_name: Any,
    val name: Any,
    val phone: String,
    val province: String,
    val province_code: Any,
    val zip: String
)



//data class LineItems(
//    val applied_discount: AppliedDiscount,
//    val custom: Boolean,
//    val fulfillable_quantity: Int,
//    val fulfillment_service: String,
//    val gift_card: Boolean,
//    val grams: Int,
//    val id: Int,
//    val name: String,
//    val price: String,
//    val product_id: Int,
//    val properties: List<Property>,
//    val quantity: Int,
//    val requires_shipping: Boolean,
//    val sku: String,
//    val tax_lines: List<TaxLine>,
//    val taxable: Boolean,
//    val title: String,
//    val variant_id: Int,
//    val variant_title: String,
//    val vendor: String
//)

data class NoteAttribute(
    val name: String,
    val value: String
)

data class OrderId(
    val id: Int
)

data class PaymentTerms(
    val amount: Int,
    val currency: String,
    val due_in_days: Int,
    val payment_schedules: List<PaymentSchedule>,
    val payment_terms_name: String,
    val payment_terms_type: String
)

data class ShippingAddress(
    val address1: String,
    val address2: String,
    val city: String,
    val company: Any,
    val country: String,
    val country_code: String,
    val first_name: String,
    val last_name: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val phone: String,
    val province: String,
    val province_code: String,
    val zip: String
)

data class ShippingLine(
    val handle: String,
    val price: Int,
    val title: String
)

data class TaxLineX(
    val price: Double,
    val rate: Double,
    val title: String
)

class Addresses


class TaxExemptions

data class Property(
    val name: String,
    val value: String
)

data class TaxLine(
    val price: String,
    val rate: String,
    val title: String
)

data class PaymentSchedule(
    val amount: Int,
    val completed_at: String,
    val currency: String,
    val due_at: String,
    val expected_payment_method: String,
    val issued_at: String
)