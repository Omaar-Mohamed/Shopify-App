package com.example.shopify_app.features.personal_details.ui

import android.location.Address
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.Locale

@Composable
fun PersonalDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        UpperSection()
        Spacer(modifier = modifier.height(15.dp))
        MidSection()
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Addresses",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LazyColumn(
            modifier = modifier.height(250.dp)
        ) {
            items(3){
                AddressCard(address = Address(Locale.getDefault()))
            }
        }

        Button(onClick = {
            navController.navigate("address")
        }) {
            Text(text = "Address")
        }
    }
}
@Composable
@Preview(showSystemUi = true)
fun PersonalDetailsScreenPreview(
    modifier: Modifier = Modifier
){
    PersonalDetailsScreen(navController = rememberNavController())
}