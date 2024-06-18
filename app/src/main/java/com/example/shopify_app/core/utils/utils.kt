package com.example.shopify_app.core.utils

import android.annotation.SuppressLint
import com.example.shopify_app.core.models.Currency


@SuppressLint("DefaultLocale")
fun priceConversion(price : String, currency: Currency) : String{
    val realValue = if(currency == Currency.USD){
        price.toDouble()
    }else {
        price.toDouble() * 50
    }
    return String.format("%.1f",realValue)
}