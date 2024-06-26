package com.example.shopify_app.features.personal_details.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AddressResponse(
    val addresses : List<AddressX>
)
data class PostAddressRequest(
    val address : AddressX
)
data class PostAddressResponse(
    val customer_address : AddressX
)
@Parcelize
data class AddressX(
    val address1: String,
    var address2: String,
    var city: String,
    val company: String,
    var country: String,
    var country_code: String,
    var country_name: String,
    val customer_id: Long,
    val default: Boolean? = null,
    val first_name: String,
    val id: Long?,
    val last_name: String,
    val name: String,
    val phone: String,
    var province: String,
    val province_code: String,
    val zip: String
) : Parcelable
