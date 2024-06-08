package com.example.shopify_app.features.myOrders.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R

data class Order(
    val orderNumber: String,
    val orderDate: String,
    val total: String,
    val imageRes: Int // Resource ID for the image
)

@Composable
fun OrderScreen(orders: List<Order>) {
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
                onClick = {  },
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
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_profile), // Replace with your profile image
//                        contentDescription = "Profile",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clip(CircleShape)
//                    )
                }
            }
        }

        // List of orders
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) { order ->
                OrderCard(
                    orderNumber = order.orderNumber,
                    orderDate = order.orderDate,
                    total = order.total,
                    imageRes = order.imageRes
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    val sampleOrders = listOf(
        Order(
            orderNumber = "Order_No1",
            orderDate = "Date 01/01/2024",
            total = "Total $100",
            imageRes = R.drawable.img // Replace with an actual drawable resource
        ),
        Order(
            orderNumber = "Order_No2",
            orderDate = "Date 02/01/2024",
            total = "Total $200",
            imageRes = R.drawable.img // Replace with an actual drawable resource
        ),
        Order(
            orderNumber = "Order_No3",
            orderDate = "Date 03/01/2024",
            total = "Total $300",
            imageRes = R.drawable.img // Replace with an actual drawable resource
        )
        // Add more sample orders as needed
    )

    OrderScreen(orders = sampleOrders)
}

