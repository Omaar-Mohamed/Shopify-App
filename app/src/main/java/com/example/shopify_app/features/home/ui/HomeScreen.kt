package com.example.shopify_app.features.home.ui

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.R
import androidx.navigation.NavHostController

import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.widgets.ProductCard
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.repo.HomeRepo
import com.example.shopify_app.features.home.data.repo.HomeRepoImpl
import com.example.shopify_app.features.home.viewmodel.HomeViewModel
import com.example.shopify_app.features.home.viewmodel.HomeViewModelFactory
//import com.example.shopify_app.features.products.ui.FakeProduct



@Composable
fun PromotionCardList(priceRulesState: ApiState<PriceRulesResponse>) {
    when (priceRulesState) {
        is ApiState.Loading -> {
            LoadingView()
        }
        is ApiState.Failure -> {
            ErrorView(priceRulesState.error)
        }
        is ApiState.Success<PriceRulesResponse> -> {
            val priceRules = priceRulesState.data.price_rules
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(priceRules) { priceRule ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        PromotionCard(priceRule = priceRule)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ShimmerEffect()
    }
}


fun Modifier.shimmer(): Modifier = this.then(
    Modifier.background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            start = Offset(0f, 0f),
            end = Offset(200f, 200f)
        ),
        shape = RoundedCornerShape(16.dp)
    )
)

@Composable
fun ShimmerEffect() {
    val transition = rememberInfiniteTransition()
    val shimmerTranslateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .shimmer()
    )
}

@Composable
fun ErrorView(error: Throwable) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Error loading promotions: ${error.message}",
            color = Color.Red
        )
    }
}

@Composable
fun ProductCardList(products: ApiState<ProductsResponse>) {
    when (products) {
        is ApiState.Loading -> {
            LoadingView()
        }

        is ApiState.Failure -> {
            ErrorView(products.error)
        }

        is ApiState.Success<ProductsResponse> -> {
            val products = products.data.products
//            ProductCardList(products)


            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(products) { product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}
//val sampleBrands = listOf(
//    Brand(name = "Nike", imageRes = R.drawable.nike), // Replace with actual drawable resources
//    Brand(name = "Adidas", imageRes = R.drawable.adidas),
//    Brand(name = "Puma", imageRes = R.drawable.nike),
//    Brand(name = "Reebok", imageRes = R.drawable.nike)
//    // Add more sample brands as needed
//)
//val fakeProducts = listOf(
////    FakeProduct("The Marc Jacobs", "Traveler Tote", "$195.00", R.drawable.img),
////    FakeProduct("Another Product", "Description", "$99.00", R.drawable.img),
////    FakeProduct("Third Product", "Description", "$250.00", R.drawable.img),
//    // Add more products as needed
//)
@Composable
fun HomeScreen(
    navController: NavHostController,
    repo: HomeRepo = HomeRepoImpl.getInstance(AppRemoteDataSourseImpl)
) {
    // Initialize the HomeViewModel with the factory
    val factory = HomeViewModelFactory(repo)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    // Trigger data fetching when the composable is first composed
    LaunchedEffect(Unit) {
        viewModel.getPriceRules()
        viewModel.getSmartCollections()
        viewModel.getProducts()
    }

    // Collect the priceRules state from the ViewModel
    val priceRulesState by viewModel.priceRules.collectAsState()
    val smartCollectionsState by viewModel.smartCollections.collectAsState()
    val productsState by viewModel.products.collectAsState()

    // Create the main UI
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            HomeTopSection(navController)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Promotions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            PromotionCardList(priceRulesState)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            ProductCardList(productsState)
            Spacer(modifier = Modifier.height(16.dp))
            BrandList(smartCollectionsState)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
//    HomeScreen()
}