package com.example.shopify_app.features.categories.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.shopify_app.features.categories.data.model.CustomCollection

@Composable
fun CategoryCard(
    category: CustomCollection,
    navController: NavHostController,
    ) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("products_screen/${category.id}/${category.handle}/${"category"}")
            },

        shape = CircleShape,
        color = Color.Black // Set the card background color to black
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter: Painter = rememberImagePainter(category.image.src)

            // Display the image using the Image composable
            Image(
                painter = painter,
                contentDescription = null, // Content description can be set if needed
//                modifier = modifier.size(24.dp) // Set the size of the image
                modifier = Modifier.size(40.dp) // Set the size of the image
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = category.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.body_html,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryCardPreview() {
//    CategoryCard()
}
