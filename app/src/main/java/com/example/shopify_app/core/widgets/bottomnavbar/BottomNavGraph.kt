package com.example.shopify_app.core.widgets.bottomnavbar

import android.location.Address
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify_app.features.ProductDetails.ui.ProductDetailScreen
import com.example.shopify_app.features.cart.ui.CartScreen
import com.example.shopify_app.features.categories.ui.CategoryScreen
import com.example.shopify_app.features.home.ui.HomeScreen
import com.example.shopify_app.features.products.ui.ProductGridScreen
import com.example.shopify_app.features.payment.ui.PaymentScreen
import com.example.shopify_app.features.personal_details.ui.AddressScreen
import com.example.shopify_app.features.personal_details.ui.PersonalDetailsScreen
import com.example.shopify_app.features.profile.ui.ProfileScreen
import com.example.shopify_app.features.settings.ui.SettingsScreen
import com.example.shopify_app.features.wishList.ui.WishListScreen
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            ) // Pass the NavController here
        }
        composable(route = BottomBarScreen.Cart.route) {
            CartScreen(navController = navController)
        }
        composable(route = BottomBarScreen.WishList.route) {
            WishListScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable("category") {
            CategoryScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("productDetails_screen") {
            ProductDetailScreen(navController = navController)
        }
        composable("products_screen/{collectionId}/{categoryTag}/{fromWhatScreen}") { backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId")
            val categoryTag = backStackEntry.arguments?.getString("categoryTag")
            val fromWhatScreen = backStackEntry.arguments?.getString("fromWhatScreen")
            ProductGridScreen(
                navController = navController,
                collectionId = collectionId,
                categoryTag = categoryTag,
                fromWhatScreen = fromWhatScreen
            )
        }
        composable("payment") {
            PaymentScreen()
        }
        composable("address") {
            AddressScreen(
                address = Address(Locale.getDefault()).apply {
                    latitude = 31.1248
                    longitude = 29.7812
                },
                navController = navController
            )
        }
        composable("personal_details") {
            PersonalDetailsScreen(navController = navController)
        }
    }
}
