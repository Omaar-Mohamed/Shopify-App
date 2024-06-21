package com.example.shopify_app.features.payment.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.shopify_app.R
import com.example.shopify_app.core.networking.RetrofitHelper
import com.example.shopify_app.core.networking.StripeServices
import com.example.shopify_app.core.networking.stripeService
import com.example.shopify_app.core.utils.PUBLISHED_KEY
import com.example.shopify_app.features.payment.data.PaymentMethod
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        PaymentConfiguration.init(context, "pk_test_51PTyhCKETkVYJYruNJuoAIWJjUmMTYNG8sUUFLGCafj3zBvu5OwV7YY4H2S2bhXHqrFK41a2XOlfy62R4sLHFamR00ZLBPvYAV")
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            PaymentSheetResult.Canceled -> {
                Log.i("PaymentSheetResult", "Payment was canceled")
            }
            PaymentSheetResult.Completed -> {
                Toast.makeText(context, "Payment completed", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                paymentSheetResult.error.printStackTrace()
                Log.e("PaymentSheetResult", "Payment failed: ${paymentSheetResult.error}")
                Toast.makeText(context, "Payment failed: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var paymentMethod by rememberSaveable { mutableStateOf(PaymentMethod.PAYMENT_CARDS) }
    val coroutineScope = rememberCoroutineScope()
    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
    var clientSecret by rememberSaveable { mutableStateOf("") }
    var ephemeralKey by rememberSaveable { mutableStateOf("") }
    var customerId by rememberSaveable { mutableStateOf("") }
    val services = RetrofitHelper.retrofitStripe.create(StripeServices::class.java)

    fun getPaymentIntent(customerId: String, ephemeralKey: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                Log.i("TAG", "getPaymentIntent: customerId $customerId ")
                val response = services.getPaymentIntent(
                    customerId,
                    "20000",
                    "egp"
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        clientSecret = response.body()!!.client_secret
                        Log.i("PaymentIntent", "Client Secret obtained: $clientSecret")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("PaymentIntent", "Failed to get client secret: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("PaymentIntent", "Exception: ${e.message}", e)
            }
        }
    }

    fun getEphemeralKey(id: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = services.getEphemeral(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        ephemeralKey = response.body()!!.id
                        Log.i("EphemeralKey", "Ephemeral Key obtained: $ephemeralKey")
                        getPaymentIntent(id, response.body()!!.id)
                    } else {
                        Log.e("EphemeralKey", "Failed to get ephemeral key: ${response}")
                    }
                }
            } catch (e: Exception) {
                Log.e("EphemeralKey", "Exception: ${e.message}", e)
            }
        }
    }

    fun getCustomerId() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = services.getCustomer()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        customerId = response.body()!!.id
                        Log.i("CustomerId", "Customer ID obtained: $customerId")
                        getEphemeralKey(response.body()!!.id)
                    } else {
                        Log.e("CustomerId", "Failed to get customer ID: ${response.errorBody()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("CustomerId", "Exception: ${e.message}", e)
            }
        }
    }

    LaunchedEffect(Unit) {
        getCustomerId()
    }

    fun paymentFlow() {
        if (clientSecret.isNotEmpty() && customerId.isNotEmpty() && ephemeralKey.isNotEmpty()) {
            Log.i("PaymentFlow", "Presenting Payment Sheet with clientSecret: $clientSecret, customerId: $customerId, ephemeralKey: $ephemeralKey")
            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                PaymentSheet.Configuration(
                    "Shopify",
                    PaymentSheet.CustomerConfiguration(
                        customerId, ephemeralKey
                    )
                )
            )
        } else {
            Log.e("PaymentFlow", "Missing required information for payment flow")
        }
    }

    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = { /* Handle back navigation */ }) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Payment",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        PaymentCard(
            isSelected = paymentMethod == PaymentMethod.PAYMENT_CARDS,
            paymentName = "Payment Cards",
            imageVector = Icons.Rounded.CreditCard
        ) {
            paymentMethod = PaymentMethod.PAYMENT_CARDS
        }
        Spacer(modifier = Modifier.height(15.dp))
        PaymentCard(
            isSelected = paymentMethod == PaymentMethod.CASH_ON_DELIVERY,
            paymentName = "Cash on delivery",
            imageVector = Icons.Outlined.Payments
        ) {
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row {
            TextButton(onClick = { /* Handle cancellation */ }) {
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
                onClick = {
                    when (paymentMethod) {
                        PaymentMethod.PAYMENT_CARDS -> {
                            Log.i("PaymentFlow", "PaymentScreen customer: $customerId")
                            Log.i("PaymentFlow", "PaymentScreen ephemeral: $ephemeralKey")
                            Log.i("PaymentFlow", "PaymentScreen secret: $clientSecret")
                            paymentFlow()
                        }
                        PaymentMethod.CASH_ON_DELIVERY -> {
                            // Handle Cash on Delivery logic
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
            ) {
                Text(text = "Confirm", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }
    }
}




@Composable
@Preview(showSystemUi = true)
fun PaymentScreenPreview(
    modifier: Modifier = Modifier
) {
    PaymentScreen()
}

//private fun createStripeCustomerId(context: Context, onResult: (customerId: String, ephemeralKey: String, clientSecret: String) -> Unit) {
//    val request: StringRequest = object : StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
//        Response.Listener { response ->
//            try {
//                val jsonObject = JSONObject(response)
//                val customerId = jsonObject.getString("id")
//                Log.i("id", "createStripeCustomerId: $customerId")
//                getEphemeralKey(context, customerId, onResult)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        },
//        Response.ErrorListener {
//            // Handle error
//            it.printStackTrace()
//        }) {
//        @Throws(AuthFailureError::class)
//        override fun getHeaders(): Map<String, String> {
//            val header: HashMap<String, String> = HashMap()
//            header["Authorization"] = "Bearer $SECRET_KEY"
//            return header
//        }
//    }
//
//    val requestQueue = Volley.newRequestQueue(context)
//    requestQueue.add(request)
//}
//
//private fun getEphemeralKey(context: Context, customerId: String, onResult: (customerId: String, ephemeralKey: String, clientSecret: String) -> Unit) {
//    val request: StringRequest = object : StringRequest(
//        Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
//        Response.Listener { response ->
//            try {
//                val jsonObject = JSONObject(response)
//                val ephemeralKey = jsonObject.getString("id")
//                getClientSecret(context, customerId, ephemeralKey, onResult)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        },
//        Response.ErrorListener {
//            // Handle error
//            it.printStackTrace()
//        }) {
//        @Throws(AuthFailureError::class)
//        override fun getHeaders(): Map<String, String> {
//            val header: HashMap<String, String> = HashMap()
//            header["Authorization"] = "Bearer $SECRET_KEY"
//            header["Stripe-Version"] = "2020-08-27"
//            return header
//        }
//
//        override fun getParams(): Map<String, String> {
//            val param: HashMap<String, String> = HashMap()
//            param["customer"] = customerId
//            return param
//        }
//    }
//
//    val requestQueue = Volley.newRequestQueue(context)
//    requestQueue.add(request)
//}
//
//private fun getClientSecret(context: Context, customerId: String, ephemeralKey: String, onResult: (customerId: String, ephemeralKey: String, clientSecret: String) -> Unit) {
//    val request: StringRequest = object : StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
//        Response.Listener { response ->
//            try {
//                val jsonObject = JSONObject(response)
//                val clientSecret = jsonObject.getString("client_secret")
//                Log.i("secret", "getClientSecret: $clientSecret")
//                onResult(customerId, ephemeralKey, clientSecret)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        },
//        Response.ErrorListener { error ->
//            error.printStackTrace()
//        }) {
//        @Throws(AuthFailureError::class)
//        override fun getHeaders(): Map<String, String> {
//            val header: HashMap<String, String> = HashMap()
//            header["Authorization"] = "Bearer $SECRET_KEY"
//            return header
//        }
//
//        override fun getParams(): Map<String, String> {
//            val param: HashMap<String, String> = HashMap()
//            param["customer"] = customerId
//            val amount = (1000) // replace this with your amount logic
//            param["amount"] = amount.toString()
//            param["currency"] = "USD"
//            param["automatic_payment_methods[enabled]"] = "true"
//            return param
//        }
//    }
//
//    val requestQueue = Volley.newRequestQueue(context)
//    requestQueue.add(request)
//}
