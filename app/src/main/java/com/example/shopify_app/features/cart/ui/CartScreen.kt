package com.example.shopify_app.features.cart.ui

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Add your Composables here
            CartHeader(
            )
            Spacer(modifier = modifier.padding(15.dp))
            Text(
                text = "My Cart",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = modifier.heightIn(10.dp))
            LazyColumn(
                modifier = modifier.heightIn(max = 500.dp, min = 350.dp)
            ) {
                items(3){
                    Spacer(modifier = Modifier.heightIn(10.dp))
                    CartCard(name = "ahmed")
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            PromoCodeField()
            Spacer(modifier = modifier.weight(1f ))
            BottomCartSection()
        }
}
@Composable
@Preview(showSystemUi = true)
fun CartScreenPreview(){
    CartScreen()
}