package com.example.shopify_app.features.signup.data.model

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    val email: String,
    val first_name: String,
    @SerializedName("note") var password: String,
    val tags: String = "",
    var multipass_identifier: String = ""
)