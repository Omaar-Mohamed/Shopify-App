package com.example.shopify_app.features.personal_details.ui

import LocationViewModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    address: Address?,
    locationViewModel: LocationViewModel = viewModel(),
    navController: NavController
) {
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

    var saveAddress by remember { mutableStateOf<Address>(address ?: Address(Locale.getDefault())) }
    var savedLatLng by rememberSaveable {
        mutableStateOf(if (address != null) LatLng(address.latitude, address.longitude) else latLng)
    }

    Log.i("TAG", "AddressScreen: default address is : $savedLatLng")
    LaunchedEffect(fullAddress) {
        if (fullAddress != null) {
            saveAddress = fullAddress!!
        }
    }

    if (!permissionGranted) {
        LaunchedEffect(Unit) {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(savedLatLng, 10f)
    }

    val markerState = remember { mutableStateOf(MarkerState(position = savedLatLng)) }

    LaunchedEffect(latLng) {
        if (address == null) {
            savedLatLng = latLng
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
            markerState.value = MarkerState(latLng)
        }
    }

    var countryName by rememberSaveable { mutableStateOf(saveAddress.countryName ?: "") }
    var locality by rememberSaveable { mutableStateOf(saveAddress.locality ?: "") }
    var adminArea by rememberSaveable { mutableStateOf(saveAddress.adminArea ?: "") }
    var addressLine by rememberSaveable { mutableStateOf(saveAddress.getAddressLine(0) ?: "") }

    var countryError by rememberSaveable { mutableStateOf(false) }
    var localityError by rememberSaveable { mutableStateOf(false) }
    var adminAreaError by rememberSaveable { mutableStateOf(false) }
    var addressLineError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(saveAddress) {
        countryName = saveAddress.countryName ?: ""
        locality = saveAddress.locality ?: ""
        adminArea = saveAddress.adminArea ?: ""
        addressLine = saveAddress.getAddressLine(0) ?: ""
    }

    LaunchedEffect(savedLatLng) {
        markerState.value = MarkerState(savedLatLng)
        saveAddress = locationViewModel.getAddress(savedLatLng) ?: Address(Locale.getDefault())
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
                    savedLatLng = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                    markerState.value = MarkerState(latLng)
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
        OutlinedTextField(
            value = locality,
            onValueChange = {
                locality = it
                localityError = it.isBlank()
            },
            label = { Text(text = "State/Region") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth(),
            isError = localityError,
            readOnly = true
        )
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
                    saveAddress.countryName = countryName
                    saveAddress.locality = locality
                    saveAddress.adminArea = adminArea
                    saveAddress.setAddressLine(0, addressLine)
                    Log.i("TAG", "AddressScreen: $saveAddress")

                    coroutineScope.launch {
                        val isValid = validateAddress(saveAddress)
                        if (isValid) {
                            Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
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

fun validateAddress(address: Address): Boolean {
    return address.countryName.isNotBlank() &&
            address.locality.isNotBlank() &&
            address.adminArea.isNotBlank() &&
            address.getAddressLine(0).isNotBlank()
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