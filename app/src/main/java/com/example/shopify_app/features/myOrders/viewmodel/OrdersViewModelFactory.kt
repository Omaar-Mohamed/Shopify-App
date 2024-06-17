package com.example.shopify_app.features.myOrders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo

class OrdersViewModelFactory(
    private val repository: OrdersRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            OrdersViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
