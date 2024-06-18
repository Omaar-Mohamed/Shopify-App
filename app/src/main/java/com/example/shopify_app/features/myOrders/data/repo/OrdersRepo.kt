package com.example.shopify_app.features.myOrders.data.repo

import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import kotlinx.coroutines.flow.Flow

interface OrdersRepo {
    suspend fun getOrders(): Flow<OrdersResponse>?

    suspend fun getOrderDetails(orderId: Long): Flow<OrderDetailsResponse>
}