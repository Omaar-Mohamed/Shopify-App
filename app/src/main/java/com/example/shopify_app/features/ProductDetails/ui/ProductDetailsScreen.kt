package com.example.shopify_app.features.ProductDetails.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.ProductDetails.viewmodel.ProductDetailsViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.ProductDetailsViewModelFactory
import com.example.shopify_app.features.Review.data.Review
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import kotlin.random.Random

@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productId: String?,
    repo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl),
    sharedViewModel: SettingsViewModel = viewModel(),
    snackBar : SnackbarHostState
) {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected) {
        val factory = ProductDetailsViewModelFactory(repo)
        val viewModel: ProductDetailsViewModel = viewModel(factory = factory)

        val draftViewModel : DraftViewModel = viewModel(factory = DraftViewModelFactory(repo))
        LaunchedEffect(Unit) {
            productId?.let {
                viewModel.getProductDetails(it)
            }
        }

        val productState by viewModel.product.collectAsState()
        var index by rememberSaveable {
            mutableIntStateOf(0)
        }
        when (productState) {
            is ApiState.Loading -> {
                LoadingView()
            }
            is ApiState.Failure -> {
                ErrorView((productState as ApiState.Failure).error)
            }
            is ApiState.Success<ProductDetailResponse> -> {
                val product = (productState as ApiState.Success<ProductDetailResponse>).data.product
                val reviews = Review.reviewsList().shuffled(Random)
                val selectedReviews = reviews.take(3)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    // Product Image
                    item {
                        ProductTopSection(product ,draftViewModel,navController)
                        SliderShow(product)
                        ProductInfo(product){
                            index = it
                        }
                        Log.i("variant", "ProductDetailScreen: index is $index")
                        ProductPriceAndCart(snackBarHostState = snackBar, product = product, draftViewModel = draftViewModel, sharedViewmodel =  sharedViewModel, variantIndex = index)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Reviews Client",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp)
                            )
                            Text(
                                text = "See More",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable(onClick = {
                                        navController.navigate("review_screen")
                                    })
                            )
                        }
                    }
                    items(3) {index->
                        ReviewCard(selectedReviews[index])
                    }

                }
            }
        }
    }
    else{
        UnavailableInternet()
    }

}

//@Composable
//fun ProductDetailScreenP(
//    navController: NavHostController,
//    productId: String?,
//    repo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl),
//) {
//
//    val factory = ProductDetailsViewModelFactory(repo)
//    val viewModel: ProductDetailsViewModel = viewModel(factory = factory)
//
//    LaunchedEffect(Unit) {
//        productId?.let {
//            viewModel.getProductDetails(it)
//        }
//    }
//
//    val productState by viewModel.product.collectAsState()
//
//    when (productState) {
//        is ApiState.Loading -> {
//            LoadingView()
//        }
//        is ApiState.Failure -> {
//            ErrorView((productState as ApiState.Failure).error)
//        }
//        is ApiState.Success<ProductDetailResponse> -> {
//            val product = (productState as ApiState.Success<ProductDetailResponse>).data.product
//            val reviews = Review.reviewsList().shuffled(Random)
//            val selectedReviews = reviews.take(3)
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//                    .padding(16.dp)
//            ) {
//                // Product Image
//                item {
//                    ProductTopSection(product,navController)
//                    SliderShow(product)
//                    ProductInfo(product)
//                    ProductPriceAndCart(product)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "Reviews Client",
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp
//                        ),
//                        modifier = Modifier
//                            .padding(start = 16.dp)
//
//                    )
//                }
//                items(3) {index->
//                    ReviewCard(selectedReviews[index])
//                }
//
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun PreviewProductDetailScreen() {
//    ProductDetailScreenP(rememberNavController(),"0")
}