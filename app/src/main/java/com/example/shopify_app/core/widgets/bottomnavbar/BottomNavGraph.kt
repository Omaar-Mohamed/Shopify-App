package com.example.shopify_app.core.widgets.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify_app.features.cart.ui.CartScreen
import com.example.shopify_app.features.categories.ui.CategoryScreen
import com.example.shopify_app.features.home.ui.HomeScreen
import com.example.shopify_app.features.settings.ui.SettingsScreen
import com.example.shopify_app.features.wishList.ui.WishListScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = navController) // Pass the NavController here
        }
        composable(route = BottomBarScreen.Cart.route) {
            CartScreen()
        }
        composable(route = BottomBarScreen.WishList.route) {
            WishListScreen()
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
        composable("category") {
            CategoryScreen(navController = navController)
        }
    }
}
