package com.example.shopify_app.features.personal_details.ui

import android.location.Address
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AlignHorizontalLeft
import androidx.compose.material.icons.automirrored.filled.Dvr
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.Locale

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address,
    onClick : ()->Unit = {}
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = onClick
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Country, City, State/Region",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AddressCardPreview(

){
    AddressCard(address = Address(Locale.getDefault()))
}