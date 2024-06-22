package com.example.shopify_app.features.wishList.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify_app.R
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
    val favoriteDraft : ApiState<DraftOrderResponse> by draftViewModel.favoriteProduct.collectAsState()

    coroutineScope.launch{
        customerStore.getFavoriteId.collect{
            if (it != null) {
                draftFavoriteId = it
            }
        }
    }
    LaunchedEffect(Unit){
        draftViewModel.getFavoriteProduct(id = draftFavoriteId)
    }

    var searchQuery by remember { mutableStateOf("") }

    if(draftFavoriteId != "") {
        when (favoriteDraft) {
            is ApiState.Loading -> {
                LoadingView()
            }

            is ApiState.Failure -> {
                ErrorView((favoriteDraft as ApiState.Failure).error)
            }

            is ApiState.Success<DraftOrderResponse> -> {
                val products =
                    (favoriteDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items
                val filteredProducts = products.filter { product ->
                    product.title.contains(searchQuery, ignoreCase = true)
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        SearchBar(onSearchQueryChange = { query -> searchQuery = query })
                        Text(
                            text = "WishList",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(filteredProducts) {
                        ProductCard(draftFavoriteId, draftViewModel, it, navController)
                    }
                }
            }

            else -> {}
        }
    }

    else{
        var dialog by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(dialog){
                AlertDialog(
                    title = { Text(
                        text = "Guest Mode",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )},
                    text = { Text(text = "Please first login to enable add and review your wishlist.")},
                    onDismissRequest = {dialog = false},
                    confirmButton = {
                        Button(
                            onClick = {
                                navController.navigate("logout")
                            }
                            ,colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(text = "Login")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {dialog = false}
                            ,colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(text = "Cancel")
                        }
                    },
                    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.nowishlist),
                contentDescription = "No Favourites",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "No WishList",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Please first login to enable add and review your wishlist.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWishListScreen() {
    //WishListScreen()
}