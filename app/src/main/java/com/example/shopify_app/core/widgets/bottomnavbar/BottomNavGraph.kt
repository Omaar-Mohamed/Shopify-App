package com.example.shopify_app.core.widgets.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify_app.features.cart.ui.CartScreen
import com.example.shopify_app.features.home.ui.HomeScreen
import com.example.shopify_app.features.settings.ui.SettingsScreen
import com.example.shopify_app.features.wishList.ui.WishListScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    // Add your Composables here
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        // Add your Composables here
        composable(route = BottomBarScreen.Home.route) {
            // Add your Composables here
            HomeScreen()
        }
        composable(route = BottomBarScreen.Cart.route) {
            // Add your Composables here
            CartScreen()
        }
        composable(route = BottomBarScreen.WishList.route) {
            // Add your Composables here
            WishListScreen()
        }
        composable(route = BottomBarScreen.Settings.route) {
            // Add your Composables here
            SettingsScreen()
        }
    }
}