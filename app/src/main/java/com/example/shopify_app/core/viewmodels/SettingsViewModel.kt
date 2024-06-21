package com.example.shopify_app.core.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify_app.core.datastore.SettingsDataStore
import com.example.shopify_app.core.models.AppMode
import com.example.shopify_app.core.models.ConversionResponse
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.models.Language
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.core.networking.AppRemoteDataSourseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val settingsStore = SettingsDataStore(application.applicationContext)
    private val _currency = MutableStateFlow(Currency.USD)
    val currency : StateFlow<Currency> = _currency

    private val _conversionRate = MutableStateFlow<ApiState<ConversionResponse>>(ApiState.Loading)
    val conversionRate : StateFlow<ApiState<ConversionResponse>> = _conversionRate

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language : StateFlow<Language> = _language

    private val _darkMode = MutableStateFlow(false)
    val darkMode : StateFlow<Boolean> = _darkMode

    init {
        viewModelScope.launch {
            launch {
                settingsStore.currencyFlow.collect{
                    if(it.equals("USD",ignoreCase = true)){
                        _currency.value = Currency.USD
                    }else{
                        _currency.value = Currency.EGP
                    }
                }
            }
            launch {
                settingsStore.languageFlow.collect{
                    if(it.equals("english",ignoreCase = true)){
                        _language.value = Language.ENGLISH
                    }else{
                        _language.value = Language.ARABIC
                    }
                }
            }
            launch {
                getConversionRate()
            }
        }
    }

    fun updateCurrency(currency: Currency){
        _currency.value =  currency
        viewModelScope.launch{
            settingsStore.saveCurrency(currency)
        }
    }

    fun updateLanguage(language: Language){
        _language.value = language
        viewModelScope.launch {
            settingsStore.saveLanguage(language)
        }
    }

    fun switchAppMode(){
        _darkMode.value = !_darkMode.value
    }

    fun getConversionRate(base : String = "EGP" , to : String = "USD"){
        viewModelScope.launch(Dispatchers.IO) {
            AppRemoteDataSourseImpl.getConversionRate(base,to)
                .catch {
                    it.printStackTrace()
                    Log.i("conversion", "getConversionRate: failed")
                    Log.i("conversion", "getConversionRate: ${it}")
                    _conversionRate.value = ApiState.Failure(it)
                }.collect{
                    Log.i("conversion", "getConversionRate: success $it")
                    _conversionRate.value = ApiState.Success(it)
            }
        }
    }
}