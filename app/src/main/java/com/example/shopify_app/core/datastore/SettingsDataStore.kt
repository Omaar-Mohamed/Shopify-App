package com.example.shopify_app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shopify_app.core.models.Currency
import com.example.shopify_app.core.models.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val languageKey = stringPreferencesKey("language")
        val currencyKey = stringPreferencesKey("currency")
    }
    suspend fun saveLanguage(language: Language){
        context.dataStore.edit { settings ->
            settings[languageKey] = language.name
        }
    }


    val languageFlow : Flow<String> = context.dataStore.data.map{settings->
        settings[languageKey] ?: "English"
    }

    suspend fun saveCurrency(currency: Currency){
        context.dataStore.edit {settings->
            settings[currencyKey] = currency.name
        }
    }

    val currencyFlow : Flow<String> = context.dataStore.data.map {settings->
        settings[currencyKey] ?: "EGP"
    }



}