package com.example.shopify_app.features.ProductDetails.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.features.ProductDetails.data.model.Product
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModelFactory
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Property

@Composable
fun ProductPriceAndCart(
    product: Product,
    draftViewModel: DraftViewModel ,
    repo : ProductsDetailsRepo = ProductsDetailsRepoImpl(AppRemoteDataSourseImpl)
    ){
    val storeCustomerEmail = StoreCustomerEmail(LocalContext.current)
    var draftId by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        storeCustomerEmail.getOrderId.collect{
            draftId = it
        }
    }
//    val draftResponse by draftViewModel.cartDraft.collectAsState()
//    draftViewModel.getDraftOrder(draftId)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.variants[0].price,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Button(
            onClick = {
                val lineItem = LineItem(
                    properties = listOf(Property("image", value = product.image.src)),
                    variant_id = product.variants[0].id,
                    quantity = 1
                )
                draftViewModel.addLineItemToDraft(draftId, lineItem)
                Log.i("TAG", "ProductPriceAndCart: $draftId")
                Log.i("TAG", "ProductPriceAndCart: addd to cart")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Add to cart")
        }

    }
}