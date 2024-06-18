package com.example.shopify_app.core.widgets

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
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
    repo : ProductsDetailsRepo = ProductsDetailsRepoImpl(AppRemoteDataSourseImpl)
) {
    val storeCustomerEmail = StoreCustomerEmail(LocalContext.current)
    var favoriteId by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        storeCustomerEmail.getFavoriteId.collect{
            if (it != null) {
                favoriteId = it
            }
        }
    }
    val draftViewModel : DraftViewModel = viewModel(factory = DraftViewModelFactory(repo))
    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(200.dp)
            .clickable(onClick = { navController.navigate("productDetails_screen/${product.id}") })
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
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

                IconButton(
                    onClick = {
                        val lineItem = LineItem(
                            id = product.id,
                            properties = listOf(Property("image", value = product.image.src)),
                            variant_id = product.variants[0].id,
                            quantity = 1
                        )
                        draftViewModel.addLineItemToDraft(favoriteId, lineItem)
                        Log.i("TAG", "ProductPriceAndCart: add done")
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite, // Using ImageVector instead of painterResource
                        contentDescription = "Favorite",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                fontSize = 18.sp,
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.variants.firstOrNull()?.price ?: "Price not available",
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