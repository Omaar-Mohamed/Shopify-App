import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _latLng = MutableStateFlow(LatLng(0.0, 0.0))
    val latLng: StateFlow<LatLng> = _latLng

    private val _fullAddress = MutableStateFlow<Address?>(null)
    val fullAddress: StateFlow<Address?> = _fullAddress

    private val _country = MutableStateFlow("")
    val country: MutableStateFlow<String> = _country

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = getLastLocation()
                location?.let {
                    val newLatLng = LatLng(it.latitude, it.longitude)
                    _latLng.value = newLatLng
                    val fetchedAddress = getAddress(newLatLng)
                    _fullAddress.value = fetchedAddress
                    _country.value = fetchedAddress?.countryName ?: ""
                    _city.value = fetchedAddress?.locality ?: ""
                    _state.value = fetchedAddress?.adminArea ?: ""
                    _description.value = fetchedAddress?.getAddressLine(0) ?: ""
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private suspend fun getLastLocation() = suspendCancellableCoroutine { continuation ->
        val context = getApplication<Application>().applicationContext
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are not granted, resume with null or handle accordingly
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                continuation.resume(location)
            } else {
                continuation.resume(null)
            }
        }.addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getAddress(latLng: LatLng): Address? {
        val geocoder = Geocoder(getApplication<Application>().applicationContext)
        return suspendCancellableCoroutine { continuation ->
            try {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
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
}
