package com.example.shopify_app.core.widgets.bottomnavbar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.ui.ProductDetailScreen
import com.example.shopify_app.features.Review.ui.ReviewScreen
import com.example.shopify_app.features.cart.ui.CartScreen
import com.example.shopify_app.features.categories.ui.CategoryScreen
import com.example.shopify_app.features.home.ui.HomeScreen
import com.example.shopify_app.features.myOrders.data.model.Order
import com.example.shopify_app.features.myOrders.ui.orderDetails.OrderSummaryScreen
import com.example.shopify_app.features.myOrders.ui.orders.OrderScreen
import com.example.shopify_app.features.products.ui.ProductGridScreen
import com.example.shopify_app.features.payment.ui.PaymentScreen
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.ui.AddressScreen
import com.example.shopify_app.features.personal_details.ui.PersonalDetailsScreen
import com.example.shopify_app.features.profile.ui.ProfileScreen
import com.example.shopify_app.features.settings.ui.SettingsScreen
import com.example.shopify_app.features.wishList.ui.WishListScreen
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val settingsSharedViewModel : SettingsViewModel = viewModel()
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                sharedViewModel = settingsSharedViewModel
            ) // Pass the NavController here
        }
        composable(route = BottomBarScreen.Cart.route) {
            CartScreen(
                navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable(route = BottomBarScreen.WishList.route) {
            WishListScreen(
                navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable("category") {
            CategoryScreen(
                navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable("productDetails_screen/{productId}") {backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                navController = navController,
                productId = productId,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable("products_screen/{collectionId}/{categoryTag}/{fromWhatScreen}") { backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId")
            val categoryTag = backStackEntry.arguments?.getString("categoryTag")
            val fromWhatScreen = backStackEntry.arguments?.getString("fromWhatScreen")
            ProductGridScreen(
                navController = navController,
                collectionId = collectionId,
                categoryTag = categoryTag,
                fromWhatScreen = fromWhatScreen,
                sharedViewModel = settingsSharedViewModel

            )
        }
        composable("payment") {
            PaymentScreen(
                sharedViewModel = settingsSharedViewModel,
                navController = navController
            )
        }
        composable("address/{address}/{customerId}") { backStackEntry ->
            val addressJson = backStackEntry.arguments?.getString("address")
            val address : AddressX = Gson().fromJson(addressJson,AddressX::class.java)
            val customerId = backStackEntry.arguments?.getString("customerId")
            Log.i("TAG", "BottomNavGraph: $address")
            if (customerId != null) {
                AddressScreen(
                    address = address,
                    navController = navController,
                    customerId = customerId.toLong(),
                    sharedViewModel = settingsSharedViewModel
                )
            }
        }
        composable("personal_details") {
            PersonalDetailsScreen(navController = navController,
                sharedViewModel = settingsSharedViewModel
            )
        }
        composable("review_screen") {
            ReviewScreen(navController = navController)
        }
        composable("my_order_screen") {
            OrderScreen(navController)
        }
        composable(
            "order_details_screen/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId")
            OrderSummaryScreen(navController = navController, orderId = orderId)
        }
    }
    }


