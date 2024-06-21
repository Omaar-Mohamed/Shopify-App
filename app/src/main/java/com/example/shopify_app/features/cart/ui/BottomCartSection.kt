package com.example.shopify_app.features.cart.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.R
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel


@SuppressLint("DefaultLocale")
@Composable
fun BottomCartSection(
    modifier: Modifier = Modifier,
    count : Int,
    totalPrice : String,
    currency: Currency,
    navController: NavController,
    sharedViewModel: SettingsViewModel
){
//    val itemCount by rememberSaveable {
//        mutableIntStateOf(3)
//    }
//    val totalPrice by rememberSaveable {
//        mutableIntStateOf(500)
//    }
    val conversionRates by sharedViewModel.conversionRate.collectAsState()
    var priceValue by rememberSaveable {
        mutableStateOf("")
    }
    val conversionRate by sharedViewModel.conversionRate.collectAsState()
    when(conversionRate){
        is ApiState.Failure -> {
            priceValue = totalPrice
        }
        ApiState.Loading -> {

        }
        is ApiState.Success -> {
            priceValue = priceConversion(totalPrice,currency,
                (conversionRate as ApiState.Success<ConversionResponse>).data)
        }
    }
    Column(
        modifier = modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Sub Total ($count):",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = currency.name+(" $priceValue" ?: ""),
                fontSize =20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        CartProceedButton(navController = navController)
    }
}

@Composable
fun CartProceedButton(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
){
    Button(
        onClick = {navController.navigate("payment")},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Proceed to Checkout")
            Image(
                painter = painterResource(id = R.drawable.button_arrow),
                contentDescription =null,
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun BottomCartSectionPreview(
    modifier: Modifier = Modifier
){
//    BottomCartSection(navController = rememberNavController())
}