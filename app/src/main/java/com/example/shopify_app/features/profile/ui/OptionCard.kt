package com.example.shopify_app.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify_app.R

@Composable
fun OptionCard(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    optionName: String,
    onClick : ()-> Unit
){
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier
                .size(30.dp)
                .background(
                    color = Color(0xFFEEEEEE),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = optionName,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = modifier.weight(1f))
        Icon(imageVector = Icons.Default.KeyboardArrowRight ,
            contentDescription = null,
            Modifier.size(20.dp)
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun OptionCardPreview(){
    OptionCard(icon = Icons.Default.Person, optionName = "Profile", onClick = {})
}