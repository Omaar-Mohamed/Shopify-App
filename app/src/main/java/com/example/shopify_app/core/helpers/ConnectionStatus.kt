package com.example.shopify_app.core.helpers

sealed class ConnectionStatus {
    object Available :ConnectionStatus()
    object Unavailable : ConnectionStatus()
}