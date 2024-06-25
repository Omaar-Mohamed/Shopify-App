package com.example.shopify_app.features.cart.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveShoppingCart
import androidx.compose.material.icons.rounded.RemoveShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.R

import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    sharedViewModel: SettingsViewModel = viewModel(),
    repo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl)
) {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
        val currency by sharedViewModel.currency.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        val customerStore = StoreCustomerEmail(LocalContext.current)
        var draftOrderId by rememberSaveable {
            mutableStateOf("")
        }
        val draftViewModel : DraftViewModel = viewModel(factory = DraftViewModelFactory(repo))
        val cartDraft : ApiState<DraftOrderResponse>  by draftViewModel.cartDraft.collectAsState()
        var customerId by rememberSaveable {
            mutableLongStateOf(0)
        }
        coroutineScope.launch{
            launch {
                customerStore.getOrderId.collect{
                    draftOrderId = it
                }
            }
            launch {
                customerStore.getCustomerId.collect{
                    customerId = it ?: 0
                }
            }
        }
        LaunchedEffect(draftOrderId){
            draftViewModel.getDraftOrder(id = draftOrderId)
        }
        var isEmpty by rememberSaveable {
            mutableStateOf(true)
        }
        if (draftOrderId != "") {
            Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // Add your Composables here
            Text(
                text = "My Cart",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = modifier.heightIn(10.dp))
            when(cartDraft){
                is ApiState.Failure -> {
                    ErrorView((cartDraft as ApiState.Failure).error)
                }
                ApiState.Loading -> {
                    LoadingView()
                }
                is ApiState.Success -> {
                    val productList = (cartDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items
                    isEmpty = productList.isEmpty()

                    LazyColumn(
                        modifier = modifier.heightIn(max = 450.dp, min = 350.dp)
                    ) {
                        if(productList.isNotEmpty())
                        {
                            items(productList){
//                            Spacer(modifier = Modifier.heightIn(10.dp))
                                CartCard(lineItem = it, draftOrderId = draftOrderId, draftViewModel = draftViewModel,currency = currency, sharedViewModel = sharedViewModel){
                                    navController.navigate("productDetails_screen/${it.product_id}")
                                }
                            }
                        }else{
                            item {
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Icon(
                                        imageVector = Icons.Outlined.RemoveShoppingCart,
                                        contentDescription = null,
                                        modifier = modifier
                                            .fillParentMaxWidth()
                                            .fillParentMaxHeight(fraction = 0.7f)
                                            .alpha(0.5f)
                                    )
                                    Text(
                                        text = "Empty Cart",
                                        style = MaterialTheme.typography.displayLarge,
                                        modifier = modifier.alpha(0.5f)
                                    )
                                }
                            }

                        }
                    }
                    Spacer(modifier = modifier.weight(1f ))
                    PromoCodeField(draftViewModel = draftViewModel, orderId = draftOrderId)

                    BottomCartSection(customerId = customerId,enable = !isEmpty,count = productList.count(),currency = currency, totalPrice = (cartDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.subtotal_price ?: "0", navController = navController, sharedViewModel = sharedViewModel)
                }

            }
        }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nocart),
                    contentDescription = "No Favourites",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Login to view Cart",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Please first login to enable add and review your cart.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        navController.navigate("logout")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Login")
                }
            }
        }
    }else{
        UnavailableInternet()
    }
}
//@Composable
//fun CartScreenP(
//    modifier: Modifier = Modifier,
//    navController: NavController,
//) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(25.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Add your Composables here
//            CartHeader(
//            )
//            Spacer(modifier = modifier.padding(15.dp))
//            Text(
//                text = "My Cart",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.ExtraBold,
//            )
//            Spacer(modifier = modifier.heightIn(10.dp))
//            LazyColumn(
//                modifier = modifier.heightIn(max = 500.dp, min = 350.dp)
//            ) {
//                items(3){
//                    Spacer(modifier = Modifier.heightIn(10.dp))
//                    CartCard(name = "ahmed")
//                }
//            }
//            Spacer(modifier = Modifier.height(15.dp))
//            PromoCodeField()
//            Spacer(modifier = modifier.weight(1f ))
//            BottomCartSection(navController = navController)
//        }
//}
@Composable
@Preview(showSystemUi = true)
fun CartScreenPreview(){
//    CartScreenP(navController = rememberNavController())
}