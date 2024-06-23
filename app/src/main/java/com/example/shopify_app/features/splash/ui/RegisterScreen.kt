package com.example.shopify_app.features.splash.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun RegisterScreen(navController: NavController){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreCustomerEmail(context)
    val savedEmail = dataStore.getEmail.collectAsState(initial = "")
    Log.i("savedEmail", "RegisterScreen: ${savedEmail.value}")
    if (savedEmail.value != ""){
        navController.navigate("bottom_nav")
    }else{
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background), // Replace with your image resource ID
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.white_logo),
                    contentDescription ="logo",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale= ContentScale.FillHeight
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("login_screen")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Login", color = Color.Black)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("signup_screen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Sign Up")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            dataStore.setEmail("")
                            dataStore.setCustomerId(0)
                            dataStore.setFavoriteId("")
                            dataStore.setOrderId("")
                            navController.navigate("bottom_nav")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Continue as Guest", color = Color.Black)
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RegisterPreview() {

}