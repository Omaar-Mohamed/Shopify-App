package com.example.shopify_app.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.repo.HomeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepo) : ViewModel() {
    private val _priceRules: MutableStateFlow<ApiState<PriceRulesResponse>> = MutableStateFlow(ApiState.Loading)
    val priceRules: StateFlow<ApiState<PriceRulesResponse>> = _priceRules

    fun getPriceRules() {
        viewModelScope.launch {
            repository.getPriceRules()
                .catch { e -> _priceRules.value = ApiState.Failure(e) }
                .collect { data -> _priceRules.value = ApiState.Success(data) }
        }
    }
}
