package com.example.shopify_app.features.personal_details.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun getAddress(context: Context, latLng: LatLng): android.location.Address? {
    val geocoder = Geocoder(context)
    return suspendCancellableCoroutine { continuation ->
        try {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    Log.i("TAG", "getAddress: ${address.countryName}")
                    continuation.resume(address)
                } else {
                    continuation.resume(null)
                }
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}

fun getCurrentLocation(context: Context, launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>, onLocationReceived:(LatLng) -> Unit) {
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        launcher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
        Log.i("TAG", "getCurrentLocation: ")
        return
    }
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            Log.i("TAG", "getCurrentLocation: $latLng")
            onLocationReceived(latLng)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    address: Address?
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    var fullAddress by remember {
        mutableStateOf<Address?>(null)
    }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latLng by remember { mutableStateOf(LatLng(31.1248, 29.7812)) }

    if(address != null) {
        fullAddress = address
        latLng = LatLng(fullAddress!!.latitude, fullAddress!!.longitude)
        LaunchedEffect(latLng) {
            val fetchedAddress = getAddress(context, latLng)
            fullAddress = fetchedAddress
            country = fetchedAddress?.countryName ?: ""
            city = fetchedAddress?.locality ?: ""
            state = fetchedAddress?.adminArea ?: ""
            description = fetchedAddress?.getAddressLine(0) ?: ""
            Log.i("Tag", "full Address: $fetchedAddress")
            Log.i("Tag", "country: $country")
        }
    } else {
        fullAddress = null
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }
    val markerState = remember {
        mutableStateOf(MarkerState(position = latLng))
    }

    Log.i("Tag","full Address $fullAddress")
    Log.i("tag", "country $country")
    Column(
        modifier = modifier
            .padding(15.dp)
    ) {
        Box {
            GoogleMap(
                modifier = Modifier
                    .height(200.dp)
                    .clip(RoundedCornerShape(15.dp)),
                cameraPositionState = cameraPositionState,
            ) {
                Marker(
                    state = markerState.value,
                    title = "Current Location",
                    snippet = "Marker in Current Location"
                )
            }
            FloatingActionButton(
                onClick = {
                    getCurrentLocation(context, launcher) { newLatLng ->
                        coroutineScope.launch {
                            val fetchedAddress = getAddress(context, newLatLng)
                            fullAddress = fetchedAddress
                            latLng = newLatLng
                            country = fetchedAddress?.countryName ?: ""
                            city = fetchedAddress?.locality ?: ""
                            state = fetchedAddress?.adminArea ?: ""
                            description = fetchedAddress?.getAddressLine(0) ?: ""
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            markerState.value = MarkerState(latLng)
                            Log.i("Tag", "full Address: $fetchedAddress")
                            Log.i("Tag", "country: $country")
                        }
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
                Icon(imageVector = Icons.Rounded.MyLocation, contentDescription = null )
            }
        }
        Spacer(modifier = modifier.height(15.dp))
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text(text = "Country") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text(text = "City") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text(text = "State/Region") },
            trailingIcon = { Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            minLines = 3,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = modifier.padding(top = 20.dp)
        ) {
            TextButton(onClick = { /*TODO*/ }) {
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
                onClick = { /*TODO*/ },
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


//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Composable
//@Preview(showSystemUi = true)
//fun AddressScreenPreview(
//
//){
//    AddressScreen(address = null)
//}