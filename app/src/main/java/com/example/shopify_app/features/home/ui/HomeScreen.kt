package com.example.shopify_app.features.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R

@Composable
fun PromotionCardList() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(10) { // Adjust the number of items as needed
            PromotionCard()
        }
    }
}

@Composable
fun ProductCardList() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(10) { // Adjust the number of items as needed
            ProductCard()
        }
    }
}
val sampleBrands = listOf(
    Brand(name = "Nike", imageRes = R.drawable.nike), // Replace with actual drawable resources
    Brand(name = "Adidas", imageRes = R.drawable.adidas),
    Brand(name = "Puma", imageRes = R.drawable.nike),
    Brand(name = "Reebok", imageRes = R.drawable.nike)
    // Add more sample brands as needed
)
@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            HomeTopSection()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Promotions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            PromotionCardList()
            Spacer(modifier = Modifier.height(16.dp))
            BrandList(brands = sampleBrands)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}