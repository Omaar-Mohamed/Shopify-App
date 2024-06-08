package com.example.shopify_app.features.cart.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify_app.R

@Composable
fun CartHeader(
    modifier: Modifier = Modifier
){
    Row {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = null,
            Modifier
                .size(35.dp)
                .clickable {
                Log.i("TAG", "CartHeader: backArrow")
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CartHeaderPreview(
    modifier: Modifier = Modifier
){
    CartHeader()
}