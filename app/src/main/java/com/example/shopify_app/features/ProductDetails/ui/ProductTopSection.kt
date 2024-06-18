package com.example.shopify_app.features.ProductDetails.ui

import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.shopify_app.core.datastore.StoreCustomerEmail
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.ProductDetails.data.model.Product
import com.example.shopify_app.features.ProductDetails.viewmodel.DraftViewModel
import com.example.shopify_app.features.home.ui.ErrorView
import com.example.shopify_app.features.home.ui.LoadingView
import com.example.shopify_app.features.signup.data.model.DarftOrderRequest.Property
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem

@Composable
fun ProductTopSection(product: Product, draftViewModel: DraftViewModel, navController: NavHostController) {
    val storeCustomerEmail = StoreCustomerEmail(LocalContext.current)
    var draftId by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        storeCustomerEmail.getFavoriteId.collect{
            if (it != null) {
                draftId = it
            }
        }
    }
    val lineItem = LineItem(
        id = product.id,
        properties = listOf(Property("image", value = product.image.src)),
        variant_id = product.variants[0].id,
        quantity = 1
    )
    var insertShowDialog by remember { mutableStateOf(false) }

    var deleteShowDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        draftViewModel.isFavoriteLineItem(draftId,lineItem)
    }
    val favoriteState by draftViewModel.inFavorite.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(40.dp)
                .background(Color.Black, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        when(favoriteState){
            is ApiState.Loading -> {

            }
            is ApiState.Failure -> {

            }
            is ApiState.Success<Boolean> -> {
                val isFavorite = (favoriteState as ApiState.Success<Boolean>).data
                IconButton(
                    onClick = {
                        if(!isFavorite){
                            insertShowDialog = true
                        }else{
                            deleteShowDialog = true
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    if(!isFavorite){
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.Gray
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.Red
                        )
                    }

                }
            }
        }
    }

    if (insertShowDialog){
        Log.i("product", "ProductTopSection: insert dialog")
        AlertDialog(
            title = { Text(text = "Add product to wishlist")},
            text = { Text(text = "Do you want to add this product item to your wishlist ?")},
            onDismissRequest = { insertShowDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        draftViewModel.addLineItemToDraft(draftId, lineItem)
                        insertShowDialog = false
                    }
                    ,colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(
                    onClick = { insertShowDialog = false }
                    ,colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
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

    if (deleteShowDialog){
        Log.i("product", "ProductTopSection: delete dialog")
        AlertDialog(

            title = { Text(text = "Remove product from wishlist")},
            text = { Text(text = "Do you want to remove this product item from your wishlist ?")},
            onDismissRequest = { deleteShowDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        draftViewModel.removeLineItemFromDraft(draftId,lineItem)
                        deleteShowDialog = false
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
                    onClick = { deleteShowDialog = false }
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




