package com.example.shopify_app.features.cart.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
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
    val currency by sharedViewModel.currency.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val customerStore = StoreCustomerEmail(LocalContext.current)
    var draftOrderId by rememberSaveable {
        mutableStateOf("")
    }
    val draftViewModel : DraftViewModel = viewModel(factory = DraftViewModelFactory(repo))
    val cartDraft : ApiState<DraftOrderResponse>  by draftViewModel.cartDraft.collectAsState()
    coroutineScope.launch{
        customerStore.getOrderId.collect{
            draftOrderId = it
        }
    }
    LaunchedEffect(draftOrderId){
        draftViewModel.getDraftOrder(id = draftOrderId)
    }

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
                }
                ApiState.Loading -> {
                    LoadingView()
                }
                is ApiState.Success -> {
                    val productList = (cartDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items
                    LazyColumn(
                        modifier = modifier.heightIn(max = 450.dp, min = 350.dp)
                    ) {
                        items(productList){
//                            Spacer(modifier = Modifier.heightIn(10.dp))
                            CartCard(lineItem = it, draftOrderId = draftOrderId, draftViewModel = draftViewModel,currency = currency, sharedViewModel = sharedViewModel){
                                navController.navigate("productDetails_screen/${it.product_id}")
                            }
                        }
                    }
                    Spacer(modifier = modifier.weight(1f ))
                    PromoCodeField(draftViewModel = draftViewModel, orderId = draftOrderId)

                    BottomCartSection(count = productList.count(),currency = currency, totalPrice = (cartDraft as ApiState.Success<DraftOrderResponse>).data.draft_order.subtotal_price ?: "0", sharedViewModel = sharedViewModel)
                }

            }
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