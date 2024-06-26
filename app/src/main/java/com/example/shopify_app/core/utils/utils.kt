package com.example.shopify_app.core.utils

import android.annotation.SuppressLint
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.viewmodels.SettingsViewModel

const val SECRET_KEY = "sk_test_51PTyhCKETkVYJYruCUsWYOwXg9MZ2HaPNQhnwzRYbzKBXIxlxJrZzdR6By4Y5WgP6aVzf3DEEkEOcfmJBrHFRljT00kAIe2eBS"
@SuppressLint("DefaultLocale")
fun priceConversion(price : String, currency: Currency, conversionRate : ConversionResponse) : String{
    val realValue = if(currency == Currency.EGP){
        price.toDouble()
    }else {
        price.toDouble() * conversionRate.rates.USD.rate.toDouble()
    }
    return String.format("%.1f",realValue)
}