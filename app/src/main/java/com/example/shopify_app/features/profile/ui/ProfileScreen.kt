package com.example.shopify_app.features.profile.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.routing.RootNavGraph
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.personal_details.ui.UpperSection
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sharedViewModel: SettingsViewModel = viewModel()
){

    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreCustomerEmail(context)
    val savedEmail = dataStore.getEmail.collectAsState(initial = "")
    Log.i("TAG", "ProfileScreen: ${sharedViewModel.currency.collectAsState().value}")
    Column(
        modifier = modifier
            .padding(15.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier.verticalScroll(rememberScrollState())
        ) {
            UserCard()
            Spacer(modifier = modifier.height(20.dp))
            MidSection {
                OptionCard(icon = Icons.Default.Person, optionName = "Personal Details", onClick = {
                    navController.navigate("personal_details")
                })
                OptionCard(icon = Icons.Default.ShoppingCart, optionName = "My Orders", onClick = {
                    navController.navigate("my_order_screen")
                })
                OptionCard(icon = Icons.Filled.Favorite, optionName = "My Favourites", onClick = {
                    navController.navigate("wishlist")
                })
                OptionCard(
                    icon = Icons.Default.Settings,
                    optionName = "Settings",
                    onClick = { navController.navigate("settings") })
            }
            Spacer(modifier = modifier.height(20.dp))
            MidSection {
                OptionCard(icon = Icons.Default.Info, optionName = "FAQs", onClick = {})
                OptionCard(icon = Icons.Default.Lock, optionName = "Privacy Policy", onClick = {})
            }
            Spacer(modifier = modifier.height(30.dp))
            if (savedEmail.value != "") {
                Button(
                    onClick = {
                        scope.launch {
                            dataStore.setEmail("")
                            dataStore.setName("Guest")
                            dataStore.setCustomerId(0)
                            dataStore.setFavoriteId("")
                            dataStore.setOrderId("")
                            navController.navigate("logout")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Logout")
                }
            } else {
                Button(
                    onClick = {
                        navController.navigate("logout")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Login")
                }
            }
        }
    }else{
        UnavailableInternet()
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