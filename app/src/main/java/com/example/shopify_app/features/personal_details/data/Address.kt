package com.example.shopify_app.features.personal_details.data

import android.location.Geocoder
import androidx.compose.ui.layout.LayoutCoordinates
import com.google.android.gms.maps.model.LatLng

data class Address(
    val country : String,
    val city : String,
    val state : String,
    val postalCode : String,
    val coordinates: LatLng
){
}
