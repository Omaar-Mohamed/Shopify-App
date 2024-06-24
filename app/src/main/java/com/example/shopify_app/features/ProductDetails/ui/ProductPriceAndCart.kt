package com.example.shopify_app.features.ProductDetails.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.utils.priceConversion
import com.example.shopify_app.core.viewmodels.SettingsViewModel
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
    sharedViewmodel : SettingsViewModel,
    repo : ProductsDetailsRepo = ProductsDetailsRepoImpl(AppRemoteDataSourseImpl)
    ){
    val currency by sharedViewmodel.currency.collectAsState()
    var priceValue by rememberSaveable {
        mutableStateOf("")
    }
    val conversionRate by sharedViewmodel.conversionRate.collectAsState()
    when(conversionRate){
        is ApiState.Failure -> {
            priceValue = product.variants[0].price
        }
        ApiState.Loading -> {

        }
        is ApiState.Success -> {
            priceValue = priceConversion(product.variants[0].price ,currency,
                (conversionRate as ApiState.Success<ConversionResponse>).data)
        }
    }

    val storeCustomerEmail = StoreCustomerEmail(LocalContext.current)
    var draftId by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        storeCustomerEmail.getOrderId.collect{
            draftId = it
        }
    }
    val lineItem = LineItem(
        properties = listOf(Property(product.variants[0].inventory_quantity.toString(), value = product.image.src)),
        variant_id = product.variants[0].id,
        quantity = 1
    )
    val isAdded by draftViewModel.inCart.collectAsState()
    draftViewModel.isInCart(id = draftId, lineItem = lineItem)
//    val draftResponse by draftViewModel.cartDraft.collectAsState()
//    draftViewModel.getDraftOrder(draftId)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = (priceValue + " " + currency.name),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            modifier = Modifier
                .padding(start = 16.dp)
        )
        when(isAdded){
            is ApiState.Failure -> {
                (isAdded as ApiState.Failure).error.printStackTrace()
                Log.i("TAG", "ProductPriceAndCart: failed ")
            }
            ApiState.Loading -> {
                Log.i("TAG", "ProductPriceAndCart: loading")
            }
            is ApiState.Success -> {
                val flag : Boolean = (isAdded as ApiState.Success<Boolean>).data
                Button(
                    onClick = {

                        draftViewModel.addLineItemToDraft(draftId, lineItem)
                        Log.i("TAG", "ProductPriceAndCart: $draftId")
                        Log.i("TAG", "ProductPriceAndCart: addd to cart")
//                            draftViewModel.isFavoriteLineItem(id = draftId, lineItem = lineItem)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    enabled = !flag
                ) {
                    Text(text = if(!flag) "Add to cart" else "Item in cart")
                }

            }
        }

    }
}