package com.example.shopify_app.features.myOrders.ui.orderDetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.shopify_app.R

// Data class for Product items
data class Product(
    val imageRes: Int,
    val name: String,
    val description: String,
    val price: String
)

// Sample list of products
val sampleProducts = listOf(
    Product(R.drawable.img, "Roller Rabbit", "Vado Odelle Dress", "$198.00"),
    Product(R.drawable.img, "Axel Arigato", "Clean 90 Triple Sneakers", "$245.00"),
    Product(R.drawable.img, "Adidas", "UltraBoost 21", "$180.00"),
    Product(R.drawable.img, "Nike", "Air Max 270", "$150.00"),
    Product(R.drawable.img, "Puma", "RS-X Bold", "$120.00")
)

@Composable
fun OrderSummaryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)  // Overall screen padding reduced to 16.dp for a more compact look
    ) {
        // Back button and title at the top
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Order Summary",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        // Scrollable content using LazyColumn
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp), // Adjusted spacing between items to 12.dp
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Section for Delivery Address
                Text(
                    text = "Delivery Address",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                )
                DeliveryAddressCard()
            }

            item {
                // Section for Product Items
                Text(
                    text = "Product Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                )
                ProductItemsCard()
            }

            item {
                // Section for Promo Code and Total Price
                Text(
                    text = "Promo Code and Total Price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                )
                PromoCodeAndTotalPriceCard()
            }
        }
    }
}

@Composable
fun DeliveryAddressCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),  // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AddressDetail(label = "Street:", detail = "3512 Pearl Street")
            AddressDetail(label = "City:", detail = "Nagercoil")
            AddressDetail(label = "State/province/area:", detail = "Tamil Nadu")
            AddressDetail(label = "Phone number:", detail = "8870523416")
            AddressDetail(label = "Zip code:", detail = "95814")
            AddressDetail(label = "Country calling code:", detail = "+91")
            AddressDetail(label = "Country:", detail = "India")
        }
    }
}

@Composable
fun AddressDetail(label: String, detail: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) { // Increased vertical padding slightly for better spacing
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = detail,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ProductItemsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Adjusted spacing between product items to 12.dp
        ) {
            // Iterate over the sampleProducts list to display each product
            sampleProducts.forEach { product ->
                ProductItem(
                    imageRes = product.imageRes,
                    name = product.name,
                    description = product.description,
                    price = product.price
                )
            }
        }
    }
}

@Composable
fun ProductItem(imageRes: Int, name: String, description: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(64.dp), // Adjusted size to make images larger
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp)) // Increased space between image and text
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased text size for better readability
            Text(text = description, fontSize = 14.sp) // Adjusted description text size
        }
        Spacer(modifier = Modifier.width(16.dp)) // Increased space before price text
        Text(text = price, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased price text size
    }
}

@Composable
fun PromoCodeAndTotalPriceCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adjusted spacing between elements for better layout
        ) {
            // Promo Code input field inside a Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Enter Promo Code",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            // Total price text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "$443.00",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OrderSummaryScreen()
}
