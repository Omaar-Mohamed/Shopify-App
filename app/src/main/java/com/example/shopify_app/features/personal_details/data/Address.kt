package com.example.shopify_app.features.personal_details.data

import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.layout.LayoutCoordinates
import com.google.android.gms.maps.model.LatLng

data class AddressResponse(
    val addresses : List<AddressX>
)
data class AddressX(
    val address1: String,
    val address2: String,
    val city: String,
    val company: Any,
    val country: String,
    val country_code: String,
    val country_name: String,
    val customer_id: Int,
    val default: Boolean,
    val first_name: Any,
    val id: Int,
    val last_name: Any,
    val name: String,
    val phone: String,
    val province: String,
    val province_code: String,
    val zip: String
)
