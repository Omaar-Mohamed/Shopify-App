package com.example.shopify_app.features.personal_details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PersonalDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Column(
        modifier = modifier.padding(15.dp)
    ) {
        UpperSection()
        Spacer(modifier = modifier.height(15.dp))
        MidSection()
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