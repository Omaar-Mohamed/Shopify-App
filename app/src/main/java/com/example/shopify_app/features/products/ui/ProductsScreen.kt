package com.example.shopify_app.features.products.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.R

data class Product(val name: String, val description: String, val price: String, val imageResId: Int)

@Composable
fun UpperSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* Handle back icon click */ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Text(
            text = "Clothes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "XXX",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun ProductGridScreen(products: List<Product>) {
    Column {
        UpperSection()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns in the grid
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProductGridScreenPreview() {
    val products = listOf(
        Product("The Marc Jacobs", "Traveler Tote", "$195.00", R.drawable.img),
        Product("Another Product", "Description", "$99.00", R.drawable.img),
        Product("Third Product", "Description", "$250.00", R.drawable.img),
        // Add more products as needed
    )
    ProductGridScreen(products = products)
}