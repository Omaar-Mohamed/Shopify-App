package com.example.shopify_app.core.viewmodels

import androidx.lifecycle.ViewModel
import com.example.shopify_app.core.models.AppMode
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.models.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _currency = MutableStateFlow(Currency.USD)
    val currency : StateFlow<Currency> = _currency

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language : StateFlow<Language> = _language

    private val _uiMode = MutableStateFlow(AppMode.LIGHT)
    val uiMode : StateFlow<AppMode> = _uiMode

    fun updateCurrency(currency: Currency){
        _currency.value =  currency
    }

    fun updateLanguage(language: Language){
        _language.value = language
    }

    fun updateAppMode(){
        if(_uiMode.value == AppMode.LIGHT){
            _uiMode.value = AppMode.DARK
        }else{
            _uiMode.value = AppMode.LIGHT
        }
    }
}