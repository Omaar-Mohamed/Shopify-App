package com.example.shopify_app.features.ProductDetails.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProductDetailScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Product Image
        item {
            ProductTopSection(navController)
            SliderShow()
            ProductInfo()
            ProductPriceAndCart()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Reviews Client",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(start = 16.dp)

            )
        }
        items(3) {
            ReviewCard()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductDetailScreen() {
    //ProductDetailScreen()
}