package com.example.shopify_app.features.myOrders.ui.orders

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.R
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.myOrders.data.model.OrdersResponse
//import com.example.shopify_app.features.myOrders.data.model.orderRequest.LineItemRequest
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderReq
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderRequest
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepoImpl
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModel
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModelFactory
import kotlin.math.log

//data class Order(
//    val orderNumber: String,
//    val orderDate: String,
//    val total: String,
//    val imageRes: Int // Resource ID for the image
//)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderScreen(
    navController: NavController,
    sharedViewModel: SettingsViewModel = viewModel(),
    repo: OrdersRepo = OrdersRepoImpl.getInstance(AppRemoteDataSourseImpl, LocalContext.current)
) {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
        val currency by sharedViewModel.currency.collectAsState()
        val factory = OrdersViewModelFactory(repo)
        val viewModel: OrdersViewModel = viewModel(factory = factory)
//    val lineItem = LineItemRequest(variant_id = 41507308666961, quantity = 4)
//    val order = OrderReq(
//        line_items = listOf(lineItem),
//        email = "ahmed.abufoda70@gmail.com",
//        send_receipt = true
//    )
//    val orderRequest = OrderRequest(order = order)
        LaunchedEffect(Unit) {
            viewModel.getOrders()

//        viewModel.createOrder(
//            orderRequest = orderRequest
//        )
        }

        val ordersState by viewModel.orders.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Top and bottom padding
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Title
                Text(
                    text = "My Orders",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                // Profile button
                IconButton(onClick = { /* Handle profile button click */ }) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        // Profile Image placeholder
                    }
                }
            }

            // List of orders or loading/error state
            when (ordersState) {
                is ApiState.Loading -> {
                    LoadingView()
                }
                is ApiState.Failure -> {
                    ErrorView((ordersState as ApiState.Failure).error)
                }
                is ApiState.Success -> {
                    val orders = (ordersState as ApiState.Success<OrdersResponse>).data.orders
                    Log.i("ordersScreen", "OrderScreen: ${orders.size} ")
                    if(orders.isNotEmpty()){
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
//                    items(orders) { order ->
//                        OrderCard(
//                            orderNumber = order.,
//                            orderDate = order.orderDate,
//                            total = order.total,
//                            imageRes = order.imageRes
//                        )
//                    }
                            items(orders) { order ->
                                OrderCard(
                                    order = order,
                                    imageRes = R.drawable.order_done,
                                    navController = navController,
                                    sharedViewModel = sharedViewModel,
                                    currency = currency
                                )
                            }
                        }
                    }
                    else{
                        Spacer(modifier = Modifier.height(30.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_order),
                                contentDescription = "No Order",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "Empty Orders",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    else{
        UnavailableInternet()
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
//    val sampleOrders = listOf(
//        Order(
//            orderNumber = "Order_No1",
//            orderDate = "Date 01/01/2024",
//            total = "Total $100",
//            imageRes = R.drawable.img // Replace with an actual drawable resource
//        ),
//        Order(
//            orderNumber = "Order_No2",
//            orderDate = "Date 02/01/2024",
//            total = "Total $200",
//            imageRes = R.drawable.img // Replace with an actual drawable resource
//        ),
//        Order(
//            orderNumber = "Order_No3",
//            orderDate = "Date 03/01/2024",
//            total = "Total $300",
//            imageRes = R.drawable.img // Replace with an actual drawable resource
//        )
//        // Add more sample orders as needed
//    )

//    OrderScreen(orders = sampleOrders)
}

