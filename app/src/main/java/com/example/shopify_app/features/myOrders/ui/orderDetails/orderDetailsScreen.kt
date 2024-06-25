package com.example.shopify_app.features.myOrders.ui.orderDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.shopify_app.R
import com.example.shopify_app.core.helpers.ConnectionStatus
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.core.widgets.UnavailableInternet
import com.example.shopify_app.core.widgets.bottomnavbar.connectivityStatus
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.myOrders.data.model.DefaultAddress
import com.example.shopify_app.features.myOrders.data.model.LineItem
import com.example.shopify_app.features.myOrders.data.model.orderdetailsModel.OrderDetailsResponse
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepoImpl
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModel
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModelFactory

// Data class for Product items
//data class Product(
//    val imageRes: Int,
//    val name: String,
//    val description: String,
//    val price: String
//)
//
//// Sample list of products
//val sampleProducts = listOf(
//    Product(R.drawable.img, "Roller Rabbit", "Vado Odelle Dress", "$198.00"),
//    Product(R.drawable.img, "Axel Arigato", "Clean 90 Triple Sneakers", "$245.00"),
//    Product(R.drawable.img, "Adidas", "UltraBoost 21", "$180.00"),
//    Product(R.drawable.img, "Nike", "Air Max 270", "$150.00"),
//    Product(R.drawable.img, "Puma", "RS-X Bold", "$120.00")
//)

@Composable
fun OrderSummaryScreen(
    navController: NavController,
    orderId: Long?,
    repo: OrdersRepo = OrdersRepoImpl.getInstance(AppRemoteDataSourseImpl, LocalContext.current),
    sharedViewModel: SettingsViewModel = viewModel(),

    ) {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    if(isConnected){
        val currency by sharedViewModel.currency.collectAsState()
        val factory = OrdersViewModelFactory(repo)
        val viewModel: OrdersViewModel = viewModel(factory = factory)

        LaunchedEffect(Unit) {
            viewModel.getOrderDetails(orderId ?: 0)
        }

        val orderDetailsState by viewModel.orderDetails.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)  // Overall screen padding reduced to 16.dp for a more compact look
        ) {
            // Back button and title at the top
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Order Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            when (orderDetailsState) {
                is ApiState.Loading -> {
                    LoadingView()
                }

                is ApiState.Failure -> {
                    // Error view
                    ErrorView((orderDetailsState as ApiState.Failure).error)
                }

                is ApiState.Success -> {
                    // Success view
                    val orderDetailsResponse =
                        (orderDetailsState as ApiState.Success<OrderDetailsResponse>).data
                    Log.i("orderDetailsState", "Order: $orderDetailsResponse")

//                // Scrollable content using LazyColumn
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp), // Adjusted spacing between items to 12.dp
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item {
                            // Section for Delivery Address
                            Text(
                                text = "Delivery Address",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                            )
                            DeliveryAddressCard( customerEmail = orderDetailsResponse.order.email, defualtAddress = orderDetailsResponse.order.customer.default_address , customerName = orderDetailsResponse.order.customer.first_name )  // Assume order has a deliveryAddress field
                        }

                        item {
                            // Section for Product Items
                            Text(
                                text = "Product Items",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                            )
                            ProductItemsCard(orderDetailsResponse.order.line_items , currency , sharedViewModel)  // Assume order has a lineItems field
                        }

                        item {
                            // Section for Promo Code and Total Price
                            Text(
                                text = "Total Price",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp) // Adjusted padding for text
                            )
                            PromoCodeAndTotalPriceCard(
                                orderDetailsResponse.order.total_discounts,
                                orderDetailsResponse.order.subtotal_price,
                                orderDetailsResponse.order.total_tax,
                                currency,
                                sharedViewModel

                            )  // Assume order has promoCode and totalPrice fields
                        }
                    }
                }

                else -> {}
            }
        }
    }
    else{
        UnavailableInternet()
    }
}



@Composable
fun DeliveryAddressCard(defualtAddress: DefaultAddress , customerEmail: String , customerName:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),  // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AddressDetail(label = "Customer Name", detail = customerName)
            AddressDetail(label = "Customer Email", detail = customerEmail)
//            AddressDetail(label = "Phone number:", detail = defualtAddress.phone)
            AddressDetail(label = "Address:", detail = defualtAddress.address2)
//            AddressDetail(label = "Address2:", detail = defualtAddress.address2)
            AddressDetail(label = "City:", detail = defualtAddress.city)
            AddressDetail(label = "Country", detail = defualtAddress.country)
            //AddressDetail(label = "Zip code:", detail = defualtAddress.zip)
        }
    }
}

@Composable
fun AddressDetail(label: String, detail: String?) {
    val displayDetail = if (detail.isNullOrEmpty()) "---" else detail

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label ",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = displayDetail ?: "---",
            modifier = Modifier.weight(1f)
        )
    }
}



@Composable
fun ProductItemsCard(lineItems: List<LineItem> , currency: Currency , sharedViewModel: SettingsViewModel) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp) // Adjusted spacing between product items to 12.dp
        ) {
            // Iterate over the sampleProducts list to display each product
            lineItems.forEach { product ->
                ProductItem(
                    imageRes = product.properties[0].value,
                    name = product.name,
                    description = product.current_quantity.toString(),
                    price = product.price,
                    currency = currency,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}

@Composable
fun ProductItem(imageRes: String, name: String, description: String, price: String , currency: Currency , sharedViewModel: SettingsViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageRes),
            contentDescription = null,
            modifier = Modifier.size(64.dp), // Adjusted size to make images larger
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp)) // Increased space between image and text
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased text size for better readability
            Text(text = "Quantity: $description", fontSize = 14.sp) // Adjusted description text size
        }
        Spacer(modifier = Modifier.width(16.dp)) // Increased space before price text
        var priceValue by rememberSaveable {
            mutableStateOf("")
        }
        val conversionRate by sharedViewModel.conversionRate.collectAsState()
        when(conversionRate){
            is ApiState.Failure -> {
                priceValue = price
            }
            ApiState.Loading -> {

            }
            is ApiState.Success -> {
                priceValue = priceConversion(price,currency,
                    (conversionRate as ApiState.Success<ConversionResponse>).data)
            }
        }
        Text(text = (priceValue + " " + currency.name) ?:"", fontWeight = FontWeight.Bold, fontSize = 16.sp) // Increased price text size
    }
}

@Composable
fun PromoCodeAndTotalPriceCard(totalDiscounts: String, totalPrice: String, totalTax: String, currency: Currency , sharedViewModel: SettingsViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Removed padding on horizontal axis for better alignment
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adjusted spacing between elements for better layout
        ) {
            // Promo Code input field inside a Card
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                shape = RoundedCornerShape(8.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White
//                ),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.img),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(64.dp)
//                            .padding(end = 8.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                    Text(
//                        text = totalDiscounts,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp,
//                        color = Color.Black
//                    )
//                }
//            }
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = "Total Tax",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//                Text(
//                    text = totalTax,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp,
//                    color = Color.Black
//                )
//            }
            // Total price text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(
                    text = "Total Price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White

                )
                var priceValue by rememberSaveable {
                    mutableStateOf("")
                }
                val conversionRate by sharedViewModel.conversionRate.collectAsState()
                when(conversionRate){
                    is ApiState.Failure -> {
                        priceValue = totalPrice
                    }
                    ApiState.Loading -> {

                    }
                    is ApiState.Success -> {
                        priceValue = priceConversion(totalPrice,currency,
                            (conversionRate as ApiState.Success<ConversionResponse>).data)
                    }
                }
                Text(
                    text = (priceValue + " " + currency.name) ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    OrderSummaryScreen()
}
