package com.example.shopify_app.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun PrivacyPolicyScreen(navController: NavController){
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
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

        }
        Section(text = "Effective Date: 6/6/2024")
        Topic(text = "1. Introduction")
        Information(
            text = "Welcome to Shopify. This Privacy Policy explains how we collects, uses, discloses, and protects your information when you use our mobile application." +
                    "By using the App, you agree to the collection and use of information in accordance with this Privacy Policy. If you do not agree with the terms of this Privacy Policy, please do not use the App."
        )
        Topic(text = "2. Information We Collect")
        Information(
            text = "When you create an account or place an order through the App, we may collect personal information that can be used to identify you, such as:\n" +
                    "  *  Name\n" +
                    "  *  Email address\n" +
                    "  *  Mailing address\n" +
                    "  *  Phone number\n" +
                    "  *  Location data"
        )
        Topic(text = "3. How We Use Your Information")
        Information(
            text = "We use the information we collect for various purposes, including:\n" +
                    "  *  To provide and maintain the App\n" +
                    "  *  To process and fulfill your orders\n" +
                    "  *  To communicate with you, including sending order\n      confirmations and updates\n" +
                    "  *  To improve and personalize your experience\n" +
                    "  *  To analyze usage and trends to improve our services\n" +
                    "  *  To prevent fraudulent transactions and ensure the security of\n      our App"
        )
        Topic(text = "4. Information Sharing and Disclosure")
        Information(text = "We may share your information with third parties in the following circumstances:\n" +
                "  *  Service Providers: We may share your information with\n      third-party service providers who perform services on our\n      behalf, such as payment processing, shipping, and customer\n      support.\n" +
                "  *  Legal Requirements: We may disclose your information \n      if required to do so by law or in response to valid requests by\n      public authorities.\n" +
                "  *  Business Transfers: In the event of a merger, acquisition,\n      or sale of all or a portion of our assets, your information may\n      be transferred as part of the transaction."
        )
        Topic(text = "5. Security")
        Information(text = "We implement appropriate technical and organizational measures to protect your information from unauthorized access, use, or disclosure. However, no method of transmission over the internet or electronic storage is completely secure, so we cannot guarantee its absolute security.")
        Topic(text = "6. Changes to This Privacy Policy")
        Information(text = "We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page and updating the effective date. You are advised to review this Privacy Policy periodically for any changes.")
        Topic(text = "9. Contact Us")
        Information(text = "If you have any questions about this Privacy Policy, please contact us at:\n" +
                "  *  [Company Name]: Shopfiy\n" +
                "  *  [Address]: Al Agamy Alexandria\n" +
                "  *  [Email Address]: admin_support@shopify.com\n" +
                "  *  [Phone Number]: +0201011226497")
    }
}

@Composable
fun Section(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.displayLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
    )
}

@Composable
fun Topic(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
    )
}

@Composable
fun Information(text: String){
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
fun PrivacyPolicyPreview(
    modifier: Modifier = Modifier
){
    PrivacyPolicyScreen(navController = rememberNavController())
}