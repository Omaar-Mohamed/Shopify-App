package com.example.shopify_app.features.wishList.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.shopify_app.R
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Property

@Composable
fun ProductCard(draftFavoriteId: String ,draftViewModel : DraftViewModel, product : LineItem, navController: NavHostController,sharedViewmodel : SettingsViewModel) {
    var shouldShowDialog by remember { mutableStateOf(false) }
    val currency by sharedViewmodel.currency.collectAsState()
    var priceValue by rememberSaveable {
        mutableStateOf("")
    }
    val conversionRate by sharedViewmodel.conversionRate.collectAsState()
    when(conversionRate){
        is ApiState.Failure -> {
            priceValue = product.price
        }
        ApiState.Loading -> {

        }
        is ApiState.Success -> {
            priceValue = priceConversion(product.price ,currency,
                (conversionRate as ApiState.Success<ConversionResponse>).data)
        }
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(12.dp)
            .clickable(onClick = {
                navController.navigate("productDetails_screen/${product.product_id}")
            })
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = product.properties[0].value),
                contentDescription = "Order Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Text(
                    text = product.title ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.vendor.toString() ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = (priceValue + " " + currency.name) ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            IconButton(
                onClick = {
                    shouldShowDialog = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.7f))
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color.Black
                )
            }
        }
    }

    if (shouldShowDialog){
        AlertDialog(
            title = { Text(text = "Remove product from wishlist")},
            text = { Text(text = "Do you want to remove this product item from your wishlist ?")},
            onDismissRequest = { shouldShowDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        draftViewModel.removeLineItemFromFavorite(draftFavoriteId,product)
                        shouldShowDialog = false
                    }
                    ,colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(
                    onClick = { shouldShowDialog = false }
                    ,colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
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
}