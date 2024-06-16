package com.example.shopify_app.features.wishList.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.ui.ProductDetailScreen
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.wishList.data.WishListRope
import com.example.shopify_app.features.wishList.data.WishListRopeImpl
import com.example.shopify_app.features.wishList.viewmodel.WishListViewModel
import com.example.shopify_app.features.wishList.viewmodel.WishListViewModelFactory

@Composable
fun WishListScreen(
    navController: NavHostController,
    repo: WishListRope = WishListRopeImpl.getInstance(AppRemoteDataSourseImpl),
    sharedViewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val dataStore = StoreCustomerEmail(context)
    val favoriteId = dataStore.getFavoriteId.collectAsState(initial = "")


    val factory = WishListViewModelFactory(repo)
    val viewModel: WishListViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        viewModel.getFavProducts(favoriteId.toString())
    }
    val favoriteProductState by viewModel.favoriteProduct.collectAsState()
    when (favoriteProductState) {
        is ApiState.Loading -> {
            LoadingView()
        }
        is ApiState.Failure -> {
            ErrorView((favoriteProductState as ApiState.Failure).error)
        }
        is ApiState.Success<DraftOrderResponse> -> {
            val products = (favoriteProductState as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    SearchBar()
                    Text(
                        text = "WishList",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(products.size) {
                    ProductCard(products[it],navController)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewWishListScreen() {
    //WishListScreen()
}