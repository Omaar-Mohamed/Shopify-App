package com.example.shopify_app.features.payment.ui

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.models.PriceData
import com.example.shopify_app.core.models.ProductData
import com.example.shopify_app.core.models.StripeLineItem
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.ProductDetails.viewmodel.PaymentViewModelFactory
import com.example.shopify_app.features.myOrders.data.model.LineItem
import com.example.shopify_app.features.myOrders.data.model.orderRequest.LineItemRequest
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderReq
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderRequest
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepoImpl
import com.example.shopify_app.features.myOrders.ui.orderDetails.ProductItem
import com.example.shopify_app.features.myOrders.ui.orderDetails.PromoCodeAndTotalPriceCard
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModel
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModelFactory
import com.example.shopify_app.features.payment.data.PaymentMethod
import com.example.shopify_app.features.payment.data.repo.PaymentRepoImpl
import com.example.shopify_app.features.payment.viewmodels.PaymentViewModel
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SettingsViewModel = viewModel(),
    repo: OrdersRepo = OrdersRepoImpl.getInstance(AppRemoteDataSourseImpl, LocalContext.current),
    draftRepo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl),
    navController: NavHostController = rememberNavController()
){
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
        val context = LocalContext.current
        val paymentFactory = PaymentViewModelFactory(PaymentRepoImpl(AppRemoteDataSourseImpl))
        val paymentViewModel : PaymentViewModel = viewModel(factory = paymentFactory)
        val draftFactory = DraftViewModelFactory(draftRepo)
        val draftViewModel: DraftViewModel = viewModel(factory = draftFactory)
        val orders by draftViewModel.cartDraft.collectAsState()
        val factory = OrdersViewModelFactory(repo)
        val viewModel: OrdersViewModel = viewModel(factory = factory)
        val checkoutResponse by paymentViewModel.checkoutResponse.collectAsState()
        var showWebView by rememberSaveable {
            mutableStateOf(false)
        }
        var paymentUrl by rememberSaveable {
            mutableStateOf("")
        }
//    val lineItem = LineItemRequest(variant_id = 41507308666961, quantity = 4)
        var storeCustomerEmail:StoreCustomerEmail = StoreCustomerEmail(LocalContext.current)
        var draftOrderId by rememberSaveable {
            mutableStateOf("")
        }
        var customerEmail by rememberSaveable {
            mutableStateOf("")
        }
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch{
            launch {
                storeCustomerEmail.getOrderId.collect{
                    draftOrderId = it
                }
            }
            launch {
                storeCustomerEmail.getEmail.collect{
                    customerEmail = it ?: "not saved"
                }
            }
        }
        var totalPrice by rememberSaveable {
            mutableDoubleStateOf(0.0)
        }

        val lineItemRequests = mutableListOf<LineItemRequest>()
        LaunchedEffect(draftOrderId){
            draftViewModel.getDraftOrder(id = draftOrderId)
        }


        Log.i("email", "PaymentScreen: " + customerEmail)
        val order = OrderReq(
            line_items = lineItemRequests,
            email = customerEmail,
            send_receipt = true
        )
        val orderRequest = OrderRequest(order = order)

//    LaunchedEffect(Unit) {
////        viewModel.getOrders()
//
////        viewModel.createOrder(
////            orderRequest = orderRequest
////        )
//    }

        var cashEnabled by rememberSaveable {
            mutableStateOf(true)
        }
        var paymentMethod by remember {
            mutableStateOf<PaymentMethod>(PaymentMethod.PAYMENT_CARDS)
        }
        when(orders) {
            is ApiState.Loading -> {
//            Text(text = "Loading")
            }
            is ApiState.Failure -> {
//            Text(text = "Failed")
            }
            is ApiState.Success -> {
                val order = (
                        orders as ApiState.Success).data
                order.draft_order.line_items.forEach { item ->
                    val lineItemRequest = LineItemRequest(
                        variant_id = item.variant_id ?: 0,
                        quantity = item.quantity
                    )
                    Log.i("payment", "PaymentScreen: $lineItemRequest")
                    lineItemRequests.add(lineItemRequest)
                }
                totalPrice = order.draft_order.subtotal_price?.toDouble() ?: 0.0
                if(totalPrice >= 10000){
                    cashEnabled = false
                }
            }
        }

        if(!showWebView)
        {
            Column(
                modifier = modifier
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Image(painter = painterResource(id = R.drawable.back_arrow), contentDescription = null,Modifier.size(30.dp) )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Payment",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                PaymentCard(isSelected = paymentMethod == PaymentMethod.PAYMENT_CARDS, paymentName = "Payment Cards" , imageVector = Icons.Rounded.Payment){
                    paymentMethod = PaymentMethod.PAYMENT_CARDS
                }
                Spacer(modifier = modifier.height(15.dp))
                PaymentCard(enable = cashEnabled,isSelected = paymentMethod == PaymentMethod.CASH_ON_DELIVERY, paymentName = "Cash on delivery", imageVector = Icons.Outlined.Payments){
                    paymentMethod = PaymentMethod.CASH_ON_DELIVERY
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Product Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                )
                when(orders) {
                    is ApiState.Loading -> {
//            Text(text = "Loading")
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is ApiState.Failure -> {
//            Text(text = "Failed")
                    }
                    is ApiState.Success -> {
                        val order = (
                                orders as ApiState.Success).data
                        order.draft_order.line_items.forEach { item ->
                            val lineItemRequest = LineItemRequest(
                                variant_id = item.variant_id ?: 0,
                                quantity = item.quantity
                            )
                            Log.i("payment", "PaymentScreen: $lineItemRequest")
                            lineItemRequests.add(lineItemRequest)
                        }
                        totalPrice = order.draft_order.subtotal_price?.toDouble() ?: 0.0
                        PaymentProductItemsCard(lineItems = order.draft_order.line_items)
                        Text(
                            text = "Total Price",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                        )
                        TotalPriceCard(totalPrice = order.draft_order.subtotal_price!!)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                ) {
                    TextButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,

                                ),
                            modifier = Modifier.alpha(0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /*TODO*/
                            when(paymentMethod){
                                PaymentMethod.PAYMENT_CARDS -> {
                                    paymentViewModel.createCheckout(CheckoutRequest(
                                        success_url = "https://shopify_app.example.com/success",
                                        mode = "payment",
                                        line_items = listOf(
                                            StripeLineItem(
                                                quantity = 1,
                                                price_data = PriceData(
                                                    unit_amount = totalPrice.toInt() * 100,
                                                    currency = "egp",
                                                    product_data = ProductData(
                                                        name = "Total",
                                                        description = ""
                                                    )
                                                )
                                            )
                                        ),
                                        cancel_url = "https://shopify_app.example.com/cancel",
                                        customer_email = customerEmail
                                    ))
                                }
                                PaymentMethod.CASH_ON_DELIVERY -> {
                                    viewModel.createOrder(orderRequest = orderRequest)
                                    draftViewModel.clearAllInDraft(draftOrderId)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = modifier
                            .width(200.dp)
                            .height(50.dp),

                        ) {
                        Text(text = "Confirm", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }

                }
                when(checkoutResponse){
                    is ApiState.Failure -> {
                        (checkoutResponse as ApiState.Failure).error.printStackTrace()
                    }
                    ApiState.Loading -> {
                    }
                    is ApiState.Success -> {
                        Log.i("payment", "PaymentScreen:${(checkoutResponse as ApiState.Success).data.url} ")
                        showWebView = true
                        paymentUrl = (checkoutResponse as ApiState.Success<CheckoutResponse>).data.url

                    }
                }
            }
        }else{
            WebViewScreen(url = paymentUrl,
                onCancel = {
                    showWebView = false
                    navController.popBackStack()
                    Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show()
                }){
                showWebView = false
//            paymentViewModel.completeOrder(draftOrderId)
                Log.i("request", "PaymentScreen: $orderRequest")
                viewModel.createOrder(orderRequest = orderRequest)
                draftViewModel.clearAllInDraft(draftOrderId)
                navController.popBackStack("home",false)
                Toast.makeText(context, "succeded", Toast.LENGTH_SHORT).show()
            }
        }
    }else{
        UnavailableInternet()
    }
}

@Composable
fun WebViewScreen(
    url: String,
    onCancel : ()->Unit,
    onPaymentCompleted: () -> Unit // Callback to handle payment completion
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        request?.url?.let {
                            if (it.toString() == "https://shopify_app.example.com/success") {
                                onPaymentCompleted()
                                return true
                            }else
                            {
                                onCancel()
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                loadUrl(url)
            }
        }
    )
}
//@Composable
//fun cardInputFields(
//    modifier: Modifier = Modifier,
//    label: String
//){
//    OutlinedTextField(
//        value = "",
//        onValueChange = {},
//        label = {
//            Text(
//                text = label
//            )
//        },
//        modifier = Modifier.padding(bottom = 10.dp)
//    )
//}
@Composable
fun PaymentProductItemsCard(lineItems: List<com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(max = 300.dp), // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Adjusted spacing between product items to 12.dp
        ) {
            // Iterate over the sampleProducts list to display each product
            LazyColumn {
                items(lineItems){product ->
                    PaymentProductItem(
                        imageRes = product.properties[0].value,
                        name = product.name,
                        quantity = product.quantity.toString(),
                        price = product.price
                    )
                }
            }
        }
    }
}


@Composable
fun PaymentProductItem(imageRes: String, name: String, quantity: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageRes, placeholder = painterResource(id = R.drawable.img)),
            contentDescription = null,
            modifier = Modifier.size(64.dp), // Adjusted size to make images larger
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp)) // Increased space between image and text
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased text size for better readability
            Text(text = "Quantity: $quantity", fontSize = 14.sp) // Adjusted description text size
        }
        Spacer(modifier = Modifier.width(16.dp)) // Increased space before price text
        Text(text = price, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased price text size
    }
}

@Composable
fun TotalPriceCard(totalPrice: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adjusted spacing between elements for better layout
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = totalPrice,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PaymentScreenPreview(
    modifier: Modifier = Modifier
){
    PaymentScreen()
}