package com.example.shopify_app.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import  com.example.shopify_app.R
// Data class for a brand
data class Brand(
    val name: String,
    val imageRes: Int // Resource ID for the brand image
)

// Composable function for individual BrandCard
@Composable
fun BrandCard(brand: Brand) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp) // Adjust width as needed
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Adjust height as needed
            ) {
                Image(
                    painter = painterResource(id = brand.imageRes), // Replace with your image resource
                    contentDescription = "Brand Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )

//                IconButton(
//                    onClick = { /* Handle heart icon click */ },
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(8.dp)
//                        .size(24.dp)
//                        .clip(CircleShape)
//                        .background(Color.White.copy(alpha = 0.7f))
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Favorite,
//                        contentDescription = "Favorite",
//                        tint = Color.Black
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = brand.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
// Composable function for the BrandList
@Composable
fun BrandList(brands: List<Brand>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Brands",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(brands) { brand ->
                BrandCard(brand = brand)
            }
        }
    }
}

// Preview for the BrandList
@Preview(showBackground = true)
@Composable
fun BrandListPreview() {
    val sampleBrands = listOf(
        Brand(name = "Nike", imageRes = R.drawable.nike), // Replace with actual drawable resources
        Brand(name = "Adidas", imageRes = R.drawable.nike),
        Brand(name = "Puma", imageRes = R.drawable.nike),
        Brand(name = "Reebok", imageRes = R.drawable.nike)
        // Add more sample brands as needed
    )

    BrandList(brands = sampleBrands)
}
