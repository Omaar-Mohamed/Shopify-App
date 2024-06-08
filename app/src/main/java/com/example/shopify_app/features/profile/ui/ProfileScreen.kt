package com.example.shopify_app.features.profile.ui

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    Column(
        modifier = modifier
            .padding(15.dp)
    ) {
        Row {
            Image(painter = painterResource(id = R.drawable.back_arrow), contentDescription = null ,
                modifier.size(30.dp))
        }
        Spacer(modifier = modifier.height(20.dp))
        UserCard()
        Spacer(modifier = modifier.height(20.dp))
        MidSection {
            OptionCard(icon = Icons.Default.Person, optionName = "Personal Details", onClick = {})
            OptionCard(icon = Icons.Default.ShoppingCart , optionName = "My Orders" , onClick = {})
            OptionCard(icon = Icons.Filled.Favorite, optionName = "My Favourites", onClick = {})
            OptionCard(icon = Icons.Default.Settings, optionName = "Settings", onClick = { navController.navigate("settings")})
        }
        Spacer(modifier = modifier.height(20.dp))
        MidSection {
            OptionCard(icon = Icons.Default.Info, optionName = "FAQs", onClick = {})
            OptionCard(icon = Icons.Default.Lock , optionName = "Privacy Policy" , onClick = {})
        }
    }

}

@Composable
fun MidSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Surface(
        modifier = modifier,
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = modifier.padding(10.dp)
        ) {
            content()
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun MidSectionPreview(){
    ProfileScreen(navController = rememberNavController())
}