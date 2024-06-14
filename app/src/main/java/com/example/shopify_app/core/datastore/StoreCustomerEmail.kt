package com.example.shopify_app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCustomerEmail(private val context: Context) {

    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserEmail")
        val CUSTOMER_EMAIL_KEY = stringPreferencesKey("customer_email")
    }

    // to get the email
    val getEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOMER_EMAIL_KEY] ?: ""
        }

    // to save the email
    suspend fun setEmail(name: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOMER_EMAIL_KEY] = name
        }
    }


}