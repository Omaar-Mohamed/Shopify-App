package com.example.shopify_app.features.cart.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CartScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        // Add your Composables here
        Text(text = "Cart Screen")

    }
}