package com.example.shopify_app.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.features.personal_details.ui.PersonalDetailsScreen

@Composable
fun FAQsScreen(navController: NavController){
    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
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
            Text(text = "FAQs",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

        }

        Spacer(modifier = Modifier.height(15.dp))
        Header(text = "General")
        Question(text = "Q: What is this app?")
        Answers(text = "A: This app is an online shopping platform where you can browse and buy products from various categories.")
        Question(text = "Q: Is the app free to use?")
        Answers(text = "A: Yes, the app is free to download and use. However, you will need to pay for any products you purchase.")
        Header(text = "Account & Login")
        Question(text = "Q: How do I create an account?")
        Answers(text = "A: You can create an account by your personal email and filling in the required information and you will receive a verification link to verify your email and continue the registration process or  you can use your google email to create your account.")
        Question(text = "Q: Can I use the app without creating an account?")
        Answers(text = "A: You can browse products without an account just login as guest, but you will need to create one to make a purchase.")
        Header(text = "Shopping & Orders")
        Question(text = "Q: How do I search for a product?")
        Answers(text = "A: Use the search bar at the top of the home screen to enter keywords related to the product you are looking for.")
        Question(text = "Q: How do I place an order?")
        Answers(text = "A: Add items to your cart, proceed to checkout, enter your shipping details, and make the payment.")
        Header(text = "Payment")
        Question(text = "Q: What payment methods are accepted?")
        Answers(text = "A: We accept credit/debit cards and cash on delivery payment methods. The available options will be displayed at checkout.")
        Question(text = "Q: Is my payment information secure?")
        Answers(text = "A: Yes, we use industry-standard encryption to protect your payment information.")
        Header(text = "Technical Issues")
        Question(text = "Q: The app is not working properly. What should I do?")
        Answers(text = "A: Try restarting the app or your device. If the issue persists, contact our customer support.")
        Question(text = "Q: How do I update the app?")
        Answers(text = "A: Visit Google Play Store (Android) and check for updates.")
        Question(text = "Q: How can I contact customer support?")
        Answers(text = "A: You can reach out to us via the \"Contact Us\" section in the app or email us at admin_support@shopfiy.com.")

    }
}

@Composable
fun Header(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.displayLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
    )
}

@Composable
fun Question(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
    )
}

@Composable
fun Answers(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 12.sp,
            color = Color.Gray
        ),
    )
}
@Composable
@Preview(showBackground = true)
fun FAQSPreview(
    modifier: Modifier = Modifier
){
    FAQsScreen(navController = rememberNavController())
}