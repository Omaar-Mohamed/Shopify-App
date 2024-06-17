package com.example.shopify_app.features.cart.ui

import android.util.Log
import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRule
import com.example.shopify_app.features.home.data.repo.HomeRepoImpl
import com.example.shopify_app.features.home.viewmodel.HomeViewModel
import com.example.shopify_app.features.home.viewmodel.HomeViewModelFactory
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.OrderId
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeField(
    modifier : Modifier = Modifier,
    draftViewModel: DraftViewModel,
    orderId: String
) {
    val factory = HomeViewModelFactory(HomeRepoImpl.getInstance(AppRemoteDataSourseImpl))
    val homeViewModel: HomeViewModel = viewModel(factory = factory)
    val promoCode = remember { mutableStateOf("") }
    var priceRules by remember {
        mutableStateOf(listOf<PriceRule>())
    }
    var isFound by remember {
        mutableStateOf(false)
    }
    var showTrialingIcon by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        homeViewModel.getPriceRules()
        homeViewModel.priceRules.collect{
            when(it)
            {
                ApiState.Loading -> {
                    Log.d("TAG", "PromoCodeField: loading")
                }
                is ApiState.Failure -> {
                    it.error
                }
                is ApiState.Success -> {
                    priceRules = it.data.price_rules
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .background(color = Color(0xFFF3F3F3), RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = promoCode.value,
            onValueChange = {
                promoCode.value = it
            },
            placeholder = { Text("Promo Code") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            trailingIcon = {
                if (showTrialingIcon){
                    if (isFound)
                    {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }else{
                        Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null, tint = Color.Red)
                    }
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Button(
            onClick = {
                Log.i("TAG", "PromoCodeField: ${priceRules[0].title}")
                Log.i("TAG", "PromoCodeField: ${promoCode.value}")
                showTrialingIcon = true
                isFound = priceRules.any{
                    it.title.equals(promoCode.value)
                }
                if (isFound)
                {
                    Log.i("TAG", "PromoCodeField: found a match")
                    val priceRule = priceRules.first {
                        it.title == promoCode.value
                    }
                    Log.i("TAG", "PromoCodeField: $priceRule")
                    draftViewModel.addCoupon(orderId,priceRule)
                }
                else
                {
                    Log.i("TAG", "PromoCodeField: did't find that promo code")
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Apply")
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PromoCodeFieldPreview(
    modifier: Modifier = Modifier
){
//    PromoCodeField()
}