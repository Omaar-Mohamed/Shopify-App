package com.example.shopify_app.features.personal_details.ui

import LocationViewModel
import android.Manifest
import android.location.Address
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepoImpl
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    address: AddressX? = null,
    locationViewModel: LocationViewModel = viewModel(),
    customerId : Long,
    navController: NavController,
    repo : PersonalRepo = PersonalRepoImpl.getInstance(AppRemoteDataSourseImpl)
) {
    val addressViewModel : AddressViewModel = viewModel(factory = AddressViewModelFactory(repo))
    val addressId : Long? = address?.id
    val context = LocalContext.current
    val latLng by locationViewModel.latLng.collectAsState()
    val fullAddress by locationViewModel.fullAddress.collectAsState()
    Log.i("TAG", "AddressScreen: $fullAddress")
    val coroutineScope = rememberCoroutineScope()
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    var saveAddress by rememberSaveable {
        if(address!= null){
            mutableStateOf<AddressX?>(address )
        }
        else{
            mutableStateOf<AddressX?>(null)
        }
    }
    var savedLatLng by rememberSaveable {
        mutableStateOf(latLng)
    }

    Log.i("TAG", "AddressScreen: default address is : $savedLatLng")
    LaunchedEffect(fullAddress) {
        if (fullAddress != null) {
            val addressX = AddressX(
                name = "",
                address1 = "",
                address2 = fullAddress!!.getAddressLine(0),
                id = null,
                city = fullAddress!!.adminArea,
                last_name = "",
                zip = "",
                country_code = fullAddress!!.countryCode,
                country_name = fullAddress!!.countryName,
                province_code = "",
                first_name = "",
                customer_id = customerId,
                phone = "",
                province = "",
                company = "",
                country = fullAddress!!.countryName,
                default = null
            )
            saveAddress = addressX
        }
    }

    if (!permissionGranted) {
        LaunchedEffect(Unit) {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(savedLatLng, 7f)
    }

    val markerState = remember { mutableStateOf(MarkerState(position = savedLatLng)) }

    LaunchedEffect(latLng) {
        if (address == null) {
            savedLatLng = latLng
            cameraPositionState.position = CameraPosition.fromLatLngZoom(savedLatLng, 10f)
            markerState.value = MarkerState(savedLatLng)
        }
    }

    var countryName by rememberSaveable { mutableStateOf(saveAddress?.country_name ?: "") }
    var locality by rememberSaveable { mutableStateOf(saveAddress?.province ?: "") }
    var adminArea by rememberSaveable { mutableStateOf(saveAddress?.city ?: "") }
    var addressLine by rememberSaveable { mutableStateOf(saveAddress?.address2 ?: "") }

    var countryError by rememberSaveable { mutableStateOf(false) }
    var localityError by rememberSaveable { mutableStateOf(false) }
    var adminAreaError by rememberSaveable { mutableStateOf(false) }
    var addressLineError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(saveAddress) {
        countryName = saveAddress?.country_name ?: ""
        locality = saveAddress?.province ?: ""
        adminArea = saveAddress?.city ?: ""
        addressLine = saveAddress?.address2 ?: ""
    }

    LaunchedEffect(savedLatLng) {
        markerState.value = MarkerState(savedLatLng)
        val returnedAddress = locationViewModel.getAddress(savedLatLng) ?: Address(Locale.getDefault())
//        saveAddress = AddressX(
//            name = "",
//            address1 = "",
//            address2 = returnedAddress.getAddressLine(0),
//            id = 0,
//            city = returnedAddress.adminArea,
//            last_name = "",
//            zip = "",
//            country_code = returnedAddress.countryCode,
//            country_name = returnedAddress.countryName,
//            province_code = returnedAddress.locality,
//            first_name = "",
//            customer_id = 6803030212689,
//            phone = "",
//            province = returnedAddress.locality,
//            company = "",
//            country = returnedAddress.countryName,
//            default = false
//        )
    }

    Column(
        modifier = modifier.padding(15.dp)
    ) {
        Box {
            GoogleMap(
                modifier = Modifier
                    .height(200.dp)
                    .clip(RoundedCornerShape(15.dp)),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    savedLatLng = it
                    coroutineScope.launch{
                        val returnedAddress = locationViewModel.getAddress(savedLatLng)
                        countryName = returnedAddress?.countryName ?:""
//                        locality = returnedAddress?.locality ?:""
                        adminArea = returnedAddress?.adminArea ?:""
                        addressLine = returnedAddress?.getAddressLine(0) ?:""
                    }
                    Log.i("TAG", "AddressScreen: $savedLatLng")
                },
                onMyLocationClick = {}
            ) {
                Marker(
                    state = markerState.value,
                    title = "Current Location",
                    snippet = "Marker in Current Location"
                )
            }
            FloatingActionButton(
                onClick = {
                    if (!permissionGranted) {
                        launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    } else {
                        locationViewModel.getCurrentLocation()
                    }
                    coroutineScope.launch{
                        val returnedAddress = locationViewModel.getAddress(savedLatLng)
                        countryName = returnedAddress?.countryName ?:""
//                        locality = returnedAddress?.locality ?:""
                        adminArea = returnedAddress?.adminArea ?:""
                        addressLine = returnedAddress?.getAddressLine(0) ?:""
                        savedLatLng = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(savedLatLng, 10f)
                        markerState.value = MarkerState(latLng)
                    }
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 5.dp,
                ),
                modifier = modifier.padding(5.dp)
            ) {
                Icon(imageVector = Icons.Rounded.MyLocation, contentDescription = null)
            }
        }
        Spacer(modifier = modifier.height(15.dp))
        OutlinedTextField(
            value = countryName,
            onValueChange = {
                countryName = it
                countryError = it.isBlank()
            },
            label = { Text(text = "Country") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth(),
            isError = countryError,
            readOnly = true
        )
        OutlinedTextField(
            value = adminArea,
            onValueChange = {
                adminArea = it
                adminAreaError = it.isBlank()
            },
            label = { Text(text = "City") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth(),
            isError = adminAreaError,
            readOnly = true
        )
//        OutlinedTextField(
//            value = locality,
//            onValueChange = {
//                locality = it
//                localityError = it.isBlank()
//            },
//            label = { Text(text = "State/Region") },
//            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
//            modifier = modifier.fillMaxWidth(),
//            isError = localityError,
//            readOnly = true
//        )
        OutlinedTextField(
            value = addressLine,
            onValueChange = {
                addressLine = it
                addressLineError = it.isBlank()
            },
            label = { Text(text = "Description") },
            minLines = 3,
            modifier = modifier.fillMaxWidth(),
            isError = addressLineError
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = modifier.padding(top = 20.dp)
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                    ),
                    modifier = Modifier.alpha(0.5f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    saveAddress?.country_name = countryName
                    saveAddress?.country = countryName
//                    saveAddress?.province = locality
                    saveAddress?.city = adminArea
                    saveAddress?.address2 = addressLine
                    Log.i("TAG", "onSaveAddressScreen: $saveAddress")

                    coroutineScope.launch {
                        val isValid = validateAddress(saveAddress!!)
                        if (isValid) {
                            Log.i("id", "AddressScreen: $addressId")
                            if(addressId == null)
                            {
                                addressViewModel.addAddress(customerId.toString(), PostAddressRequest(saveAddress!!))
                                addressViewModel.addResponse.collect{
                                    when(it){
                                        is ApiState.Failure -> {
                                            Toast.makeText(context, "Error : ${it.error}", Toast.LENGTH_SHORT).show()
                                            it.error.printStackTrace()
                                        }
                                        ApiState.Loading -> {
                                            Toast.makeText(context, "saving", Toast.LENGTH_SHORT).show()
                                        }
                                        is ApiState.Success -> {
                                            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            }
                            else
                            {
                                addressViewModel.updateAddress(customerId.toString(),addressId.toString(),
                                    PostAddressRequest(saveAddress!!)
                                )
                                addressViewModel.updateResponse.collect{
                                    when(it){
                                        is ApiState.Failure -> {
                                            Toast.makeText(context, "Error : ${it.error}", Toast.LENGTH_SHORT).show()
                                            it.error.printStackTrace()
                                        }
                                        ApiState.Loading -> {
                                            Toast.makeText(context, "saving", Toast.LENGTH_SHORT).show()
                                        }
                                        is ApiState.Success -> {
                                            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please choose a valid location", Toast.LENGTH_SHORT).show()
                        }
                    }

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
fun putAddressIntoX(addressX: AddressX,address: Address){

}

fun validateAddress(address: AddressX): Boolean {
    return  address.country_name.isNotBlank() &&
            address.city.isNotBlank() &&
            address.address2.isNotBlank()
}


//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//suspend fun validateAddress(context: Context, address: Address): Boolean {
//    val geocoder = Geocoder(context)
//    return suspendCancellableCoroutine { continuation ->
//        try {
//            geocoder.getFromLocationName(address.getAddressLine(0), 1) { addresses ->
//                if (addresses.isNotEmpty()) {
//                    val foundAddress = addresses[0]
//                    // Compare the found address components with the provided address components
//                    val isValid = foundAddress.countryName == address.countryName &&
//                            foundAddress.locality == address.locality &&
//                            foundAddress.adminArea == address.adminArea &&
//                            foundAddress.getAddressLine(0) == address.getAddressLine(0)
//                    continuation.resume(isValid)
//                } else {
//                    continuation.resume(false)
//                }
//            }
//        } catch (e: Exception) {
//            continuation.resumeWithException(e)
//        }
//    }
//}
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Composable
//@Preview(showSystemUi = true)
//fun AddressScreenPreview(
//
//){
//    AddressScreen(address = null)
//}