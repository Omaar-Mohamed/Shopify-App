package com.example.shopify_app.features.Review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shopify_app.features.ProductDetails.ui.ReviewCard
import com.example.shopify_app.features.Review.data.Review
import kotlin.random.Random

@Composable
fun ReviewScreen(navController: NavHostController){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ){
        val reviews = Review.reviewsList().shuffled(Random)
        val numberOfReview = Random.nextInt(4, 10)
        val selectedReviews = reviews.take(numberOfReview)
        item {
            Row {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .background(Color.Black, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Reviews Client",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
        items(selectedReviews.size) {index->
            ReviewCard(selectedReviews[index])
        }
    }
}