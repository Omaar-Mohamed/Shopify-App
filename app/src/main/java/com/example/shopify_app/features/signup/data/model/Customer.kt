package com.example.shopify_app.features.signup.data.model

import com.google.gson.annotations.SerializedName

data class Customer(
    val id: Long,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("orders_count")
    val ordersCount: Long,
    val note: Any?,
    @SerializedName("multipass_identifier")
    val multipassIdentifier: Any?,
    val tags: String,
    val currency: String,
    val phone: String,
    val addresses: List<Address>,
    @SerializedName("default_address")
    val defaultAddress: DefaultAddress,
)


data class DefaultAddress(
    val id: Long,
    @SerializedName("customer_id")
    val customerId: Long,
    @SerializedName("first_name")
    val firstName: String,
    val address1: String,
    val city: String,
    val country: String,
    val zip: String,
    val phone: String,
    val name: String,
    @SerializedName("country_name")
    val countryName: String,
    val default: Boolean,
)

data class Address(
    val id: Long,
    @SerializedName("customer_id")
    val customerId: Long,
    @SerializedName("first_name")
    val firstName: String,
    val address1: String,
    val city: String,
    val country: String,
    val zip: String,
    val phone: String,
    val name: String,
    @SerializedName("country_name")
    val countryName: String,
    val default: Boolean,
)