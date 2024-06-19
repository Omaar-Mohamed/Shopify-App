package com.example.shopify_app.features.myOrders.data.repo

import android.content.Context
import android.util.Log
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderRequest
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class OrdersRepoImpl private constructor(
    private val appRemoteDataSourse: AppRemoteDataSourse,
    private val context: Context
) : OrdersRepo {

    companion object {
        private var instance: OrdersRepoImpl? = null
        fun getInstance(appRemoteDataSourse: AppRemoteDataSourse, context: Context): OrdersRepoImpl {
            if (instance == null) {
                instance = OrdersRepoImpl(appRemoteDataSourse, context)
            }
            return instance!!
        }
    }
    private val storeCustomerEmail = StoreCustomerEmail(context)

    override suspend fun getOrders(): Flow<OrdersResponse>? = flow {
        storeCustomerEmail.getCustomerId.collect { id ->
            Log.i("userId", "getOrders: $id")
            if (id != 0L) {
                val response = appRemoteDataSourse.getOrders(id)
                emitAll(response)
            } else {
                throw IllegalArgumentException("Customer ID is not valid")
            }
        }
    }.catch { e ->
        Log.e("getOrders", "Failed to get orders: ${e.message}")
        throw e
    }

    override suspend fun getOrderDetails(orderId: Long): Flow<OrderDetailsResponse> {
        return appRemoteDataSourse.getOrdersDetails(orderId)
    }

    override suspend fun createOrder(orderRequest: OrderRequest): Flow<OrderDetailsResponse> {
        return appRemoteDataSourse.createOrder(orderRequest)
    }

}