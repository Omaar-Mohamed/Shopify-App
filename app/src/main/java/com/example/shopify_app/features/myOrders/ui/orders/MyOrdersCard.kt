package com.example.shopify_app.features.myOrders.ui.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.shopify_app.R
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.myOrders.data.model.Order
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCard(order: Order, imageRes: Int, navController: NavController , currency: Currency , sharedViewModel: SettingsViewModel) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    val parsedDateTime = LocalDateTime.parse(order.created_at, formatter)

// Format the date and time separately
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val formattedDate = parsedDateTime.format(dateFormatter)
    val formattedTime = parsedDateTime.format(timeFormatter)

// Concatenate the date and time with "at" in between
    val formattedDateTime = "$formattedDate at $formattedTime"

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("order_details_screen/${order.id}")
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Order Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.order_number.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = formattedDateTime,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            var priceValue by rememberSaveable {
                mutableStateOf("")
            }
            val conversionRate by sharedViewModel.conversionRate.collectAsState()
            when(conversionRate){
                is ApiState.Failure -> {
                    priceValue = order.subtotal_price
                }
                ApiState.Loading -> {

                }
                is ApiState.Success -> {
                    priceValue = priceConversion(order.subtotal_price,currency,
                        (conversionRate as ApiState.Success<ConversionResponse>).data)
                }
            }
            Text(
                text = (priceValue + " " + currency.name) ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}


