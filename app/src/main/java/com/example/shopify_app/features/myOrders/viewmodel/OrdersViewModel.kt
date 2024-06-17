package com.example.shopify_app.features.myOrders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val repository: OrdersRepo
) : ViewModel() {
    private val _orders: MutableStateFlow<ApiState<OrdersResponse>> =
        MutableStateFlow(ApiState.Loading)
    val orders: StateFlow<ApiState<OrdersResponse>> = _orders

    private val _orderDetails: MutableStateFlow<ApiState<OrderDetailsResponse>> =
        MutableStateFlow(ApiState.Loading)
    val orderDetails: StateFlow<ApiState<OrderDetailsResponse>> = _orderDetails

    fun getOrders() {
        viewModelScope.launch {
            val response = repository.getOrders()
            response?.catch { e ->
                _orders.value = ApiState.Failure(e)
            }?.collect { data ->
                _orders.value = ApiState.Success(data)
            }
        }
    }
    fun getOrderDetails(orderId: Long) {
        viewModelScope.launch {
            repository.getOrderDetails(orderId)
                .catch { e -> _orderDetails.value = ApiState.Failure(e) }
                .collect { data -> _orderDetails.value = ApiState.Success(data) }
        }
    }
}