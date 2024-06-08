package com.example.shopify_app.features.home.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R
import kotlinx.coroutines.launch


@Composable
fun PromotionCard(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = SnackbarHostState()
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .width(300.dp), // Adjust the width as needed to fit within LazyRow
        shape = RoundedCornerShape(10.dp),
    ) {
        val clipManager : ClipboardManager = LocalClipboardManager.current
        val scope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust height as needed
        ) {
            Image(
                painter = painterResource(id = R.drawable.img), // Replace with your image resource
                contentDescription = "Promotional Bag",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column {
                    Text(
                        text = "50% Off",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Adjust color for visibility
                    )
                    Text(
                        text = "On everything today",
                        fontSize = 16.sp,
                        color = Color.White // Adjust color for visibility
                    )
                }
                Column {
                    Text(
                        text = "With code: FSCREATION",
                        fontSize = 14.sp,
                        color = Color.White // Adjust color for visibility
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            clipManager.setText(AnnotatedString("CardPromoCode"))
                            scope.launch {
                                snackBarHostState.showSnackbar("PromoCode copied ")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(
                            text = "Get Now",
                            color = Color.White
                        )
                    }
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PromotionCardPreview() {
    MaterialTheme {
        PromotionCard()
    }
}