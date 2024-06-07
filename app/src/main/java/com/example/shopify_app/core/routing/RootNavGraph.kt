package com.example.shopify_app.core.routing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.shopify_app.core.widgets.bottomnavbar.BottomNav
import com.example.shopify_app.features.categories.ui.CategoryScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "bottom_nav") {
        composable("bottom_nav") {
            BottomNav()
        }
//        composable("category") {
//            CategoryScreen()
//        }
    }
}
