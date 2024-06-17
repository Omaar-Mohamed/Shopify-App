package com.example.shopify_app.features.wishList.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.ui.ProductDetailScreen
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WishListScreen(
    navController: NavHostController,
    repo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl),
    sharedViewModel: SettingsViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val customerStore = StoreCustomerEmail(LocalContext.current)
    var draftFavoriteId by rememberSaveable {
        mutableStateOf("")
    }
    val draftViewModel : DraftViewModel = viewModel(factory = DraftViewModelFactory(repo))
    val favoriteDraft : ApiState<DraftOrderResponse> by draftViewModel.cartDraft.collectAsState()

    coroutineScope.launch{
        customerStore.getFavoriteId.collect{
            if (it != null) {
                draftFavoriteId = it
            }
        }
    }
    LaunchedEffect(draftFavoriteId){
        draftViewModel.getDraftOrder(id = draftFavoriteId)
    }

    when (favoriteDraft) {
        is ApiState.Loading -> {
            LoadingView()
        }
        is ApiState.Failure -> {
            ErrorView((favoriteDraft as ApiState.Failure).error)
        }
        is ApiState.Success<DraftOrderResponse> -> {
            val products = (favoriteDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items
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
                    ProductCard(draftFavoriteId,draftViewModel,products[it],navController)
                }
            }
        }
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWishListScreen() {
    //WishListScreen()
}