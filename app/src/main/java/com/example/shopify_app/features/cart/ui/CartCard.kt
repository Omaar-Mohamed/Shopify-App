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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.shopify_app.R
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.ui.theme.ShopifyAppTheme

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun CartCard(
    lineItem: LineItem,
    modifier: Modifier = Modifier,
    draftViewModel: DraftViewModel,
    draftOrderId : String,
    currency: Currency,
    onClick : () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 160.dp)
            .padding(10.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
        onClick = {onClick()}
//        border = BorderStroke(1.dp,Color.Black)
    ) {
        val priceValue : String = priceConversion(lineItem.price,currency)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = 90.dp, max = 160.dp)
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
                Text(
                    text = lineItem.variant_title ?: "",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.alpha(0.5F)
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = priceValue ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = modifier.weight(1f))
                    ItemCounter(lineItem = lineItem, draftOrderId = draftOrderId ,draftViewModel =draftViewModel )
                    IconButton(onClick = {
                        draftViewModel.removeLineItemFromDraft(draftOrderId,lineItem)
                    }) {
                        Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null, tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCounter(
    modifier: Modifier = Modifier,
    lineItem: LineItem,
    draftViewModel: DraftViewModel,
    draftOrderId: String
) {
    val limit : Int = lineItem.properties[0].name.toInt()
    var counter by rememberSaveable {
        mutableIntStateOf(lineItem.quantity)
    }
    LaunchedEffect(counter) {
        Log.i("tag", "ItemCounter: hifffffffffffffffffffff")
        draftViewModel.changeQuantity(lineItem = lineItem, id = draftOrderId, quantity = counter)
    }
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
                    if (counter > 0) {
                        counter--
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
//                        draftViewModel.changeQuantity(draftOrderId,lineItem,counter)
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