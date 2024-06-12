package com.example.shopify_app.features.personal_details.ui

import android.location.Address
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.launch
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
        Spacer(modifier = modifier.height((15.dp)))
        LazyColumn(
            modifier = modifier.height(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(3){
                AddressCard(address = Address(Locale.getDefault())){
                    val address = Address(Locale.getDefault()).apply {
                        latitude = 29.1245
                        longitude = 31.125
                    }

                    val addressJson = Gson().toJson(LatLng(29.1245,31.125))
                    navController.navigate("address/$addressJson")
                }
            }
            item {
                FloatingActionButton(
                    modifier = modifier.padding(10.dp),
                    shape = CircleShape,
                    onClick = { navController.navigate("address") },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null,
                        Modifier.size(30.dp))
                }
            }
        }
        Spacer(modifier = modifier.weight(1f))
        Row(
            modifier = modifier.padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
                    .width(200.dp)
                    .height(50.dp),
            ) {
                Text(text = "Save", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
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