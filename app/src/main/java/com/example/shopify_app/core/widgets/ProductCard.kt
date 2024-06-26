package com.example.shopify_app.core.widgets

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Property
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem

//import com.example.shopify_app.features.products.ui.Product

@Composable
fun ProductCard(
    product:Product,
    navController: NavController,
    repo : ProductsDetailsRepo = ProductsDetailsRepoImpl(AppRemoteDataSourseImpl),
    currency: Currency,
    sharedViewmodel : SettingsViewModel
) {
    var priceValue by rememberSaveable {
        mutableStateOf("")
    }
    val conversionRate by sharedViewmodel.conversionRate.collectAsState()
    when(conversionRate){
        is ApiState.Failure -> {
            priceValue = product.variants.firstOrNull()?.price ?: "Price not available"
        }
        ApiState.Loading -> {

        }
        is ApiState.Success -> {
            priceValue = priceConversion(product.variants.firstOrNull()?.price ?: "Price not available",currency,
                (conversionRate as ApiState.Success<ConversionResponse>).data)
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(200.dp)
            .clickable(onClick = { navController.navigate("productDetails_screen/${product.id}") })
            .clip(RoundedCornerShape(16.dp))
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = product.images[0].src), // Load image from URL
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = product.vendor,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = priceValue+" "+currency.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
//    ProductCard(
//        Product("The Marc Jacobs", "Traveler Tote", "$195.00", R.drawable.img)
//    )
}