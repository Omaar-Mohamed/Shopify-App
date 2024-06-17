package com.example.shopify_app.features.cart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.ui.theme.ShopifyAppTheme

@Composable
fun CartCard(lineItem: LineItem, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .width(350.dp)
            .height(100.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp),
            ),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(76.dp)
                .width(101.dp)
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cart_card_placeholder),
                contentDescription = null,
                modifier = modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
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
                        text = lineItem.price ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = modifier.weight(1f))
                    ItemCounter(count = lineItem.quantity)
                }
            }
        }
    }
}

@Composable
fun ItemCounter(
    modifier: Modifier = Modifier,
    count : Int
) {
    var counter by rememberSaveable {
        mutableIntStateOf(count)
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
                    counter++
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
        ItemCounter(count = 0)
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrderCartCardPreview() {
    ShopifyAppTheme {
//        CartCard(name = "ahmed")
    }
}