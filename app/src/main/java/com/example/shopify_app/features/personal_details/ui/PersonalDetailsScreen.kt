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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepoImpl
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PersonalDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    personalRepo: PersonalRepo = PersonalRepoImpl.getInstance(AppRemoteDataSourseImpl),
    sharedViewModel: SettingsViewModel = viewModel()
){
    val customerId = "6804394213457"
    val viewModel : AddressViewModel = viewModel(factory = AddressViewModelFactory(personalRepo))
    val addressList by viewModel.addresses.collectAsState()
    viewModel.getAddresses(customerId)
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
            when(addressList){
                is ApiState.Failure -> {
                    val error = (addressList as ApiState.Failure).error
                    error.printStackTrace()
                }
                ApiState.Loading -> {
                    Log.i("TAG", "PersonalDetailsScreen: loading")
                }
                is ApiState.Success -> {
                    val list = (addressList as ApiState.Success<AddressResponse>).data.addresses
                    Log.i("tag", "PersonalDetailsScreen: $list")
                    items(list){address ->
                        AddressCard(address = address, onClick ={
                                val addressJson = Gson().toJson(address)
                                navController.navigate("address/$addressJson/${address.customer_id.toString()}")
                            },
                            onDelete = {
                                viewModel.deleteAddress(address.customer_id.toString(),address.id.toString())
                            }
                        )
                    }
                }
            }
            item {
                FloatingActionButton(
                    modifier = modifier.padding(10.dp),
                    shape = CircleShape,
                    onClick = { navController.navigate("address/{}/${customerId}") },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null,
                        Modifier.size(30.dp))
                }
            }
        }
        Spacer(modifier = modifier.weight(1f))
        Row(
            modifier = modifier
                .padding(top = 20.dp)
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