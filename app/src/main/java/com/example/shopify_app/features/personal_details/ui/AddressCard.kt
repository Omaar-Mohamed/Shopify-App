package com.example.shopify_app.features.personal_details.ui

import android.location.Address
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalLeft
import androidx.compose.material.icons.automirrored.filled.Dvr
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.RestoreFromTrash
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
import java.util.Locale

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: AddressX?=null,
    addressViewModel: AddressViewModel,
    onClick : ()->Unit = {},
    onDelete: () -> Unit = {}
){
    var shouldShowDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .heightIn(min = 60.dp, max = 200.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(
            contentColor = Color.Black
        ),
        onClick = onClick
    ) {

        val isDefault = address?.default
        Column(
            modifier = modifier.padding(5.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if(isDefault!!) Icons.Rounded.LocationOn else Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = address.address2 ?:"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.width(250.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = modifier
                        .clip(RoundedCornerShape(5.dp))
                        .size(40.dp)
                    ,
                    onClick = {
                        shouldShowDialog = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                    ),
                    enabled = !isDefault
                ){
                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = null,
                        tint = if(!isDefault) MaterialTheme.colorScheme.error else Color.Transparent,
                        modifier = modifier
                            .clip(RoundedCornerShape(5.dp))
                            .fillMaxSize()
                            .padding(5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = modifier.fillMaxWidth()
            ) {

                    Button(
                        onClick = {
                            if (address != null) {
                                addressViewModel.makeAddressDefault(customerId = address.customer_id.toString(), addressId = address.id.toString())
                            }
                        },
                        modifier = modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray
                        ),
                        enabled = !isDefault!!
                    ) {
                        Text(
                            text = if(isDefault) "Default address" else "Make default",
                            fontSize = 18.sp ,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.DarkGray
                        )
                    }


            }

        }
    }
    if(shouldShowDialog)
    {
        AlertDialog(
            title = { Text(text = "Remove address")},
            text = { Text(text = "Are you sure you want to remove this address?")},
            onDismissRequest = { shouldShowDialog = false },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        onDelete()
                        shouldShowDialog = false
                    }
                    ,colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                androidx.compose.material3.Button(
                    onClick = { shouldShowDialog = false }
                    ,colors = androidx.compose.material3.ButtonDefaults.buttonColors(
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

@Composable
@Preview(showSystemUi = true)
fun AddressCardPreview(

){
//    AddressCard()
}