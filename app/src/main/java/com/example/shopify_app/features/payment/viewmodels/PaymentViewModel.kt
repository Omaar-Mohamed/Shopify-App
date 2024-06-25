package com.example.shopify_app.features.payment.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.payment.data.repo.PaymentRepo
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.log

class PaymentViewModel(
    private val repo: PaymentRepo
) : ViewModel() {
    private val _checkoutResponse: MutableStateFlow<ApiState<CheckoutResponse>> = MutableStateFlow(ApiState.Loading)
    val checkoutResponse: StateFlow<ApiState<CheckoutResponse>> = _checkoutResponse

    private val _orderResponse : MutableStateFlow<ApiState<DraftOrderResponse>> = MutableStateFlow(ApiState.Loading)
    val orderResponse : StateFlow<ApiState<DraftOrderResponse>> = _orderResponse
    fun createCheckout(checkoutRequest: CheckoutRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createCheckout(checkoutRequest)
                .catch { e ->
                    e.printStackTrace()
                    Log.e("payment", "createCheckout: ${e.cause}")
                    _checkoutResponse.value = ApiState.Failure(e)
                }
                .collect { response ->
                    _checkoutResponse.value = ApiState.Success(response)
                }
        }
    }
}