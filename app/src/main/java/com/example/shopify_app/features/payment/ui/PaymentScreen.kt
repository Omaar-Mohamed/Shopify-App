package com.example.shopify_app.features.payment.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.R
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.myOrders.data.model.orderRequest.LineItemRequest
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderReq
import com.example.shopify_app.features.myOrders.data.model.orderRequest.OrderRequest
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepo
import com.example.shopify_app.features.myOrders.data.repo.OrdersRepoImpl
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModel
import com.example.shopify_app.features.myOrders.viewmodel.OrdersViewModelFactory
import com.example.shopify_app.features.payment.data.PaymentMethod
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SettingsViewModel = viewModel(),
    repo: OrdersRepo = OrdersRepoImpl.getInstance(AppRemoteDataSourseImpl, LocalContext.current),
    draftRepo: ProductsDetailsRepo = ProductsDetailsRepoImpl.getInstance(AppRemoteDataSourseImpl)


){
    val draftFactory = DraftViewModelFactory(draftRepo)
    val draftViewModel: DraftViewModel = viewModel(factory = draftFactory)
    val orders by draftViewModel.cartDraft.collectAsState()
    val factory = OrdersViewModelFactory(repo)
    val viewModel: OrdersViewModel = viewModel(factory = factory)
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

    val lineItemRequests = mutableListOf<LineItemRequest>()
    LaunchedEffect(draftOrderId){
        draftViewModel.getDraftOrder(id = draftOrderId)
    }

    when(orders) {
        is ApiState.Loading -> {
//            Text(text = "Loading")
        }

        is ApiState.Failure -> {
//            Text(text = "Failed")
        }

        is ApiState.Success -> {
            Text(text = "Success")
            Log.i("info", "PaymentScreen: " + (orders as ApiState.Success<DraftOrderResponse>).data.draft_order.line_items[0].variant_id)
            val order = (
                     orders as ApiState.Success).data
            order.draft_order.line_items.forEach { item ->
                val lineItemRequest = LineItemRequest(
                    variant_id = item.variant_id ?: 0,
                    quantity = item.quantity
                )
                lineItemRequests.add(lineItemRequest)
            }
        }
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


    var paymentMethod by remember {
        mutableStateOf<PaymentMethod>(PaymentMethod.PAYPAL)
    }
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
        PaymentCard(isSelected = paymentMethod == PaymentMethod.PAYPAL, paymentName = "PayPal" , imageResources = R.drawable.paypal){
            paymentMethod = PaymentMethod.PAYPAL
        }
        Spacer(modifier = modifier.height(15.dp))
        PaymentCard(isSelected = paymentMethod == PaymentMethod.VISA, paymentName = "Visa", imageResources = R.drawable.visa_logo){
            paymentMethod = PaymentMethod.VISA
        }
        Spacer(modifier = modifier.height(15.dp))
        PaymentCard(isSelected = paymentMethod == PaymentMethod.CASH_ON_DELIVERY, paymentName = "Cash on delivery", imageResources = R.drawable.cod_logo){
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Card Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = "Name on the card"
                )
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
            )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = "Card number"
                )
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = "Expiry date"
                    )
                },
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .widthIn(max = 200.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = "CVV"
                    )
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            TextButton(onClick = { /*TODO*/ }) {
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
                          viewModel.createOrder(
                              orderRequest = orderRequest
                          )
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


    }


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
@Preview(showSystemUi = true)
fun PaymentScreenPreview(
    modifier: Modifier = Modifier
){
    PaymentScreen()
}