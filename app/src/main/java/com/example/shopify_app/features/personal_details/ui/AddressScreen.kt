package com.example.shopify_app.features.personal_details.ui

import LocationViewModel
import android.Manifest
import android.app.Activity
import android.content.IntentSender
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import com.example.shopify_app.core.viewmodels.SettingsViewModel
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepoImpl
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    address: AddressX? = null,
    locationViewModel: LocationViewModel = viewModel(),
    customerId: Long,
    navController: NavController,
    repo: PersonalRepo = PersonalRepoImpl.getInstance(AppRemoteDataSourseImpl),
    sharedViewModel: SettingsViewModel = viewModel()
) {
    val addressViewModel: AddressViewModel = viewModel(factory = AddressViewModelFactory(repo))
    val addressId: Long? = address?.id
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
        if (address != null) {
            mutableStateOf<AddressX?>(address)
        } else {
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
    var phoneNumber by rememberSaveable {
        mutableStateOf(saveAddress?.phone ?: "")
    }
    var userName by rememberSaveable {
        mutableStateOf(saveAddress?.name ?: "")
    }
    var countryError by rememberSaveable { mutableStateOf(false) }
    var userNameError by rememberSaveable {
        mutableStateOf(false)
    }
    var phoneNumberError by rememberSaveable {
        mutableStateOf(false)
    }
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
    }

    fun checkGpsStatus() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(context)
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, 0)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        checkGpsStatus()
    }
    var countryCode by rememberSaveable {
        mutableStateOf("")
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
                    coroutineScope.launch {
                        val returnedAddress = locationViewModel.getAddress(savedLatLng)
                        countryName = returnedAddress?.countryName ?: ""
                        adminArea = returnedAddress?.adminArea ?: ""
                        addressLine = returnedAddress?.getAddressLine(0) ?: ""
                        countryCode = returnedAddress?.countryCode ?: ""
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
                    coroutineScope.launch {
                        val returnedAddress = locationViewModel.getAddress(savedLatLng)
                        countryName = returnedAddress?.countryName ?: ""
                        adminArea = returnedAddress?.adminArea ?: ""
                        addressLine = returnedAddress?.getAddressLine(0) ?: ""
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
            value = userName,
            onValueChange ={
                userName = it
                userNameError = it.isBlank()
            },
            label = { Text(text = "Receiver Name")},
            trailingIcon = {Icon(imageVector = Icons.Outlined.Person , contentDescription = null) },
            modifier = modifier.fillMaxWidth(),
            isError = userNameError,
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange ={
                if(it.isDigitsOnly()){
                    phoneNumber = it
                }
                phoneNumberError = it.isBlank()
            },
            label = { Text(text = "Phone number")},
            trailingIcon = {Icon(imageVector = Icons.Outlined.Phone , contentDescription = null) },
            modifier = modifier.fillMaxWidth(),
            isError = phoneNumberError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
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
            val addResponse by addressViewModel.addResponse.collectAsState()
            Button(
                onClick = {
                    saveAddress?.country_name = countryName
                    saveAddress?.country = countryName
                    saveAddress?.city = adminArea
                    saveAddress?.address2 = addressLine
                    saveAddress?.country_code = countryCode
                    saveAddress?.phone = phoneNumber
                    saveAddress?.name = userName
                    Log.i("TAG", "onSaveAddressScreen: $saveAddress")

                    coroutineScope.launch(Dispatchers.IO) {
                        val isValid = validateAddress(saveAddress!!)
                        if (isValid) {
                            Log.i("id", "AddressScreen: $addressId")
                            if (addressId == null) {
                                addressViewModel.addAddress(customerId.toString(), PostAddressRequest(saveAddress!!))
                                launch {
                                    addressViewModel.addResponse.collect {
                                        when (it) {
                                            is ApiState.Failure -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "Error : ${it.error}", Toast.LENGTH_SHORT).show()
                                                }
                                                it.error.printStackTrace()
                                            }
                                            ApiState.Loading -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "saving", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            is ApiState.Success -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                }
                                            }

                                            else -> {}
                                        }
                                    }
                                }
                            } else {
                                addressViewModel.updateAddress(customerId.toString(), addressId.toString(), PostAddressRequest(saveAddress!!))
                                launch {
                                    addressViewModel.updateResponse.collect {
                                        when (it) {
                                            is ApiState.Failure -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "Error : ${it.error}", Toast.LENGTH_SHORT).show()
                                                }
                                                it.error.printStackTrace()
                                            }
                                            ApiState.Loading -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "saving", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            is ApiState.Success -> {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                }
                                            }
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

fun validateAddress(address: AddressX): Boolean {
    return address.country_name.isNotBlank() &&
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