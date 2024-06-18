package com.example.shopify_app.features.personal_details.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.AddressX
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepoImpl
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddressViewModel(
    private val personalRepoImpl: PersonalRepo
) : ViewModel() {
    private val _addresses : MutableStateFlow<ApiState<AddressResponse>> = MutableStateFlow(ApiState.Loading)
    val addresses : StateFlow<ApiState<AddressResponse>> = _addresses

    private val _addResponse : MutableStateFlow<ApiState<PostAddressResponse>> = MutableStateFlow(ApiState.Loading)
    val addResponse : StateFlow<ApiState<PostAddressResponse>> = _addResponse

    private val _deleteResponse : MutableStateFlow<ApiState<PostAddressResponse>> = MutableStateFlow(ApiState.Loading)
    val deleteResponse : StateFlow<ApiState<PostAddressResponse>> = _deleteResponse
    private val _updateResponse : MutableStateFlow<ApiState<PostAddressResponse>> = MutableStateFlow(ApiState.Loading)
    val updateResponse : StateFlow<ApiState<PostAddressResponse>> = _updateResponse
    fun getAddresses(customerId : String){
        viewModelScope.launch(Dispatchers.IO) {
            personalRepoImpl.getAddresses(customerId)
                .catch {
                    _addresses.value = ApiState.Failure(it)
                }
                .collect{
                    _addresses.value = ApiState.Success(it)
                }
        }
    }

    fun addAddress(customerId: String, address: PostAddressRequest){
        viewModelScope.launch (Dispatchers.IO){
            personalRepoImpl.addAddresses(customerId,address)
                .catch {
                    _addResponse.value = ApiState.Failure(it)
                }
                .collect{
                    _addResponse.value = ApiState.Success(it)
                }
        }
    }

    fun updateAddress(customerId: String,addressId: String,address: PostAddressRequest){
        viewModelScope.launch (Dispatchers.IO){
            personalRepoImpl.updateAddress(customerId, addressId, address)
                .catch {
                    _addResponse.value = ApiState.Failure(it)
                }
                .collect{
                    _addResponse.value = ApiState.Success(it)
                }
        }
    }

    fun deleteAddress(customerId: String,addressId: String){
        viewModelScope.launch(Dispatchers.IO) {
            personalRepoImpl.deleteAddress(customerId, addressId)
                .catch {
                _deleteResponse.value = ApiState.Failure(it)
            }
                .collect{
                    _deleteResponse.value = ApiState.Success(it)
                    getAddresses(customerId)
            }
        }
    }


}