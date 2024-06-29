package com.example.shopify_app.features.cart.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter

import com.example.shopify_app.R
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.ui.theme.ShopifyAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("DefaultLocale")
@Composable
fun CartCard(
    lineItem: LineItem,
    modifier: Modifier = Modifier,
    draftViewModel: DraftViewModel,
    draftOrderId : String,
    currency: Currency,
    sharedViewModel: SettingsViewModel,
    onClick : () -> Unit,
) {
    var shouldShowDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var isShown by rememberSaveable {
        mutableStateOf(true)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 200.dp)
            .padding(top = 5.dp, bottom = 5.dp)
        ,
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
        onClick = {onClick()},

//        border = BorderStroke(1.dp,Color.Black)
    ) {
        val limitFlag = rememberSaveable() {
            mutableStateOf(false)
        }
        var priceValue by rememberSaveable {
            mutableStateOf("")
        }

        val conversionRate by sharedViewModel.conversionRate.collectAsState()
        when(conversionRate){
            is ApiState.Failure -> {
                priceValue = lineItem.price
            }
            ApiState.Loading -> {

            }
            is ApiState.Success -> {
                priceValue = priceConversion(lineItem.price,currency,
                    (conversionRate as ApiState.Success<ConversionResponse>).data)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = 100.dp, max = 200.dp)
                .width(101.dp)
                .padding(10.dp)
        ) {
            val painter = rememberAsyncImagePainter(model = lineItem.properties[0].value)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = modifier
                    .size(height = 90.dp, width = 90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(5.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = modifier.padding(start = 6.dp, top = 2.dp)
            ) {
                Text(
                    text = lineItem.title ?: "",
                    fontSize = 14.sp,
                )
                Row {
                    Text(
                        text = lineItem.variant_title ?: "",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier.alpha(0.5F)
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$priceValue $currency" ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = modifier.weight(1f))
                    ItemCounter(lineItem = lineItem, draftOrderId = draftOrderId ,draftViewModel =draftViewModel,limitFlag = limitFlag )
                    IconButton(onClick = {
                        shouldShowDialog = true
                    }) {
                        Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null, tint = Color.Red)
                    }
                }
                if (limitFlag.value) {
                    Text(
                        text = "You have reached the limit of one product",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
        if(shouldShowDialog)
        {
            AlertDialog(
                title = { Text(text = "Remove Item")},
                text = { Text(text = "Are you sure you want to remove this item?")},
                onDismissRequest = { shouldShowDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            draftViewModel.removeLineItemFromDraft(draftOrderId,lineItem)
                            isShown = false
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
}

@Composable
fun ItemCounter(
    modifier: Modifier = Modifier,
    lineItem: LineItem,
    draftViewModel: DraftViewModel,
    draftOrderId: String,
    limitFlag : MutableState<Boolean>
) {
    val limit : Int = lineItem.properties[0].name.toInt()
    var counter by rememberSaveable {
        mutableIntStateOf(lineItem.quantity)
    }
    LaunchedEffect(counter) {
        Log.i("tag", "ItemCounter: hifffffffffffffffffffff")
        draftViewModel.changeQuantity(lineItem = lineItem, id = draftOrderId, quantity = counter)
    }
    Column {
        Surface(
            modifier = modifier
                .width(100.dp)
                .clip(RoundedCornerShape(30.dp)),
            color = Color(0xFFEEEEEE)
        ) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        if (counter > 1) {
                            counter--
                            limitFlag.value = false
//                        draftViewModel.changeQuantity(id = draftOrderId, lineItem = lineItem, quantity = counter)
                        }
                    },
                    modifier = Modifier.width(30.dp)
                ) {
                    Text(
                        text = "-",
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = counter.toString(),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = {
                        if(counter < limit && counter < 5 )
                        {
                            counter++
                            limitFlag.value = false
//                        draftViewModel.changeQuantity(draftOrderId,lineItem,counter)
                        }else{
                            limitFlag.value = true
                        }
                    },
                    modifier = Modifier.width(30.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 14.sp
                    )
                }
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun ItemCounterPreview(){
    ShopifyAppTheme {
//        ItemCounter()
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrderCartCardPreview() {
    ShopifyAppTheme {
//        CartCard(name = "ahmed")
    }
}