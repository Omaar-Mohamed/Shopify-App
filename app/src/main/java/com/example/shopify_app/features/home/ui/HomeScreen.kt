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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun PromotionCardList(
    priceRulesState: ApiState<PriceRulesResponse>,
    snackbarHostState: SnackbarHostState,
    searchQuery: String // Added search query parameter
) {
    when (priceRulesState) {
        is ApiState.Loading -> {
            LoadingView()
        }
        is ApiState.Failure -> {
            ErrorView(priceRulesState.error)
        }
        is ApiState.Success<PriceRulesResponse> -> {
            val priceRules = priceRulesState.data.price_rules

            // Filter price rules by search query
            val filteredPriceRules = priceRules.filter { priceRule ->
                priceRule.title.contains(searchQuery, ignoreCase = true)
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPriceRules) { priceRule ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        PromotionCard(priceRule = priceRule, snackBarHostState = snackbarHostState)
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
fun ProductCardList(
    products: ApiState<ProductsResponse>,
    navController: NavHostController,
    searchQuery: String // Added search query parameter
) {
    when (products) {
        is ApiState.Loading -> {
            LoadingView()
        }
        is ApiState.Failure -> {
            ErrorView(products.error)
        }
        is ApiState.Success<ProductsResponse> -> {
            val products = products.data.products

            // Filter products by search query
            val filteredProducts = products.filter { product ->
                product.title.contains(searchQuery, ignoreCase = true)
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(product = product, navController)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    repo: HomeRepo = HomeRepoImpl.getInstance(AppRemoteDataSourseImpl),
    snackbarHostState: SnackbarHostState
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

    // Collect the priceRules, smartCollections, and products states from the ViewModel
    val priceRulesState by viewModel.priceRules.collectAsState()
    val smartCollectionsState by viewModel.smartCollections.collectAsState()
    val productsState by viewModel.products.collectAsState()

    // State for search query
    var searchQuery by remember { mutableStateOf("") }

    // Create the main UI
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            HomeTopSection(
                navController = navController,
                onSearchQueryChange = { query -> searchQuery = query }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Promotions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            PromotionCardList(priceRulesState, snackbarHostState, searchQuery)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            ProductCardList(productsState, navController, searchQuery)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Brands",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            BrandList(smartCollectionsState, navController, searchQuery)
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
//    HomeScreen()
}