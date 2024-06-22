package com.example.shopify_app.features.payment.ui

import android.content.res.Resources
import android.text.BoringLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.StreamType
import com.example.shopify_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    isSelected : Boolean,
    paymentName : String,
    imageVector : ImageVector,
    onClick : ()->Unit
) {
    val backGroundColor =
        if(isSelected){
            Color.Black
        }else{
            Color.White
        }
    val textColor =
        if (isSelected){
            Color.White
        }else{
            Color.Black
        }
    val imageBackGroundColor =
        if (isSelected) {
            Color.White
        }else{
            Color.LightGray
        }
    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Black else Color.White,
        ) ,
        elevation = CardDefaults.cardElevation(
            pressedElevation = 8.dp,
            defaultElevation = 5.dp
        ),
        onClick = {
            onClick()
        }

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
        ){
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = modifier
                    .size(40.dp)
                    .background(
                        color = imageBackGroundColor,
                        shape = CircleShape,
                    )
                    .padding(5.dp)
                ,
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = paymentName,
                color = textColor
            )
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = isSelected , onClick = { /*TODO*/ },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = Color.Black,
                )
            )
        }
    }
}
@Composable
@Preview(showSystemUi = true)
fun PaymentCardPreview(

){
    PaymentCard(isSelected = true, paymentName = "Visa", imageVector = Icons.Default.CreditCard){}
}