package com.example.shopify_app.features.myOrders.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.shopify_app.R

@Composable
fun OrderCard(orderNumber: String, orderDate: String, total: String, imageRes: Int) {
    Card(
        shape = RoundedCornerShape(16.dp), // Rounded corners
//        elevation = 5.dp, // Elevation for shadow effect
        modifier = Modifier
            .padding(8.dp) // Outer padding for the card
            .fillMaxWidth(), // Make the card fill the width of the parent
                elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
                )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp) // Inner padding inside the card
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Center align items vertically
        ) {
            // Image on the left side
            Image(
                painter = painterResource(id = imageRes), // Replace with your image resource
                contentDescription = "Order Image",
                contentScale = ContentScale.Crop, // Scale the image to fill the bounds while preserving aspect ratio
                modifier = Modifier
                    .size(60.dp) // Size of the image
                    .clip(RoundedCornerShape(10.dp)) // Clip the image to be rounded
            )

            Spacer(modifier = Modifier.width(16.dp)) // Space between image and text

            // Column for order details
            Column(
                modifier = Modifier
                    .weight(1f) // Take all available horizontal space except for total text
            ) {
                Text(
                    text = orderNumber,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = orderDate,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Text for total amount on the right side
            Text(
                text = total,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically) // Align the total text to the center vertically
            )
        }
    }
}

