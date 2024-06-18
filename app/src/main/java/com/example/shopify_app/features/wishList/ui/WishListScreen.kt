package com.example.shopify_app.features.wishList.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.ui.ProductDetailScreen

@Composable
fun WishListScreen(
    navController: NavHostController,
    sharedViewModel: SettingsViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            SearchBar()
            Text(
                text = "WishList",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(7) {
            ProductCard(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWishListScreen() {
    //WishListScreen()
}