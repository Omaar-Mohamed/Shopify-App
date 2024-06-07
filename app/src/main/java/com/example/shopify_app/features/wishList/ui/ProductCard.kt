package com.example.shopify_app.features.wishList.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R

@Composable
fun ProductCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.tshirt),
                contentDescription = "Order Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Text(
                    text = "Roller Rabbit",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "Vado Odelle Dress",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$198.00",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Button(
                onClick = { /* Handle Add to Cart */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.Bottom)
            ) {
                Text(text = "Add to cart")
            }
        }
    }
}