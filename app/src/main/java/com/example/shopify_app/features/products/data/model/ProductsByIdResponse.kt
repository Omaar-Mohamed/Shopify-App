package com.example.shopify_app.features.products.data.model

import com.example.shopify_app.features.home.data.models.ProductsResponse.Product

data class ProductsByIdResponse(
    val products: List<Product>
)