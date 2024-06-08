package com.example.shopify_app.features.payment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(15.dp)
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Image(painter = painterResource(id = R.drawable.back_arrow), contentDescription = null,Modifier.size(30.dp) )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Payment",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        PaymentCard(isSelected = false, paymentName = "PayPal" , imageResources = R.drawable.paypal)
        Spacer(modifier = modifier.height(15.dp))
        PaymentCard(isSelected = true, paymentName = "Visa", imageResources = R.drawable.visa_logo)
        Spacer(modifier = modifier.height(15.dp))
        PaymentCard(isSelected = false, paymentName = "Cash on delivery", imageResources = R.drawable.cod_logo)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Card Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = "Name on the card"
                )
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
            )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    text = "Card number"
                )
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = "Expiry date"
                    )
                },
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .widthIn(max = 200.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text(
                        text = "CVV"
                    )
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,

                    ),
                    modifier = Modifier.alpha(0.5f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = modifier.width(200.dp)
                    .height(50.dp),

            ) {
                Text(text = "Confirm", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }

    }


}
//@Composable
//fun cardInputFields(
//    modifier: Modifier = Modifier,
//    label: String
//){
//    OutlinedTextField(
//        value = "",
//        onValueChange = {},
//        label = {
//            Text(
//                text = label
//            )
//        },
//        modifier = Modifier.padding(bottom = 10.dp)
//    )
//}
@Composable
@Preview(showSystemUi = true)
fun PaymentScreenPreview(
    modifier: Modifier = Modifier
){
    PaymentScreen()
}