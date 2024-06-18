package com.example.shopify_app.features.signup.data.model.CustomerRequest

import com.example.shopify_app.features.signup.data.model.CustomerRequest.AddresseX

data class CustomerXX(
    val addresses: List<AddresseX> = listOf(),
    val email: String,
    val first_name: String,
    val last_name: String ="",
    val password: String,
    val password_confirmation: String,
    val phone: String="",
    val send_email_welcome: Boolean = false,
    val verified_email: Boolean = false
)