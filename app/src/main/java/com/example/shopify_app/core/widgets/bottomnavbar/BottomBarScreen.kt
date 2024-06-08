package com.example.shopify_app.core.widgets.bottomnavbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val icon_focused: ImageVector
){
    object Home : BottomBarScreen(
        route = "home",
        icon = Icons.Filled.Home,
        title = "Home",
        icon_focused = Icons.Default.Home
    )
    object Cart : BottomBarScreen(
        route = "cart",
        icon = Icons.Filled.ShoppingCart,
        title = "Cart",
        icon_focused = Icons.Default.ShoppingCart
    )
    object WishList : BottomBarScreen(
        route = "wishlist",
        icon = Icons.Filled.Favorite,
        title = "WishList",
        icon_focused = Icons.Default.Favorite
    )
    object Profile : BottomBarScreen(
        route = "profile",
        icon = Icons.Filled.Person,
        title = "Me",
        icon_focused = Icons.Default.Person
    )
}