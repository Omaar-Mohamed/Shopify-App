package com.example.shopify_app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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
        val CUSTOMER_NAME_KEY = stringPreferencesKey("customer_name")
        val CUSTOMER_ID_KEY = longPreferencesKey("customer_id")
        val FAVORITE_ID_KEY = stringPreferencesKey("favorite_id")
        val ORDER_ID_KEY = stringPreferencesKey("order_id")
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

    // to get the Name
    val getName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOMER_NAME_KEY] ?: "Guest"
        }

    // to save the Name
    suspend fun setName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOMER_NAME_KEY] = name
        }
    }

    // to get customer id
    val getCustomerId: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOMER_ID_KEY] ?: 0
        }

    // to save customer id
    suspend fun setCustomerId(id: Long) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOMER_ID_KEY] = id
        }
    }

    // to get favorite id
    val getFavoriteId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITE_ID_KEY] ?: ""
        }

    // to save favorite id
    suspend fun setFavoriteId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_ID_KEY] = id
        }
    }

    // to get order id
    val getOrderId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ORDER_ID_KEY] ?: ""
        }

    // to save order id
    suspend fun setOrderId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[ORDER_ID_KEY] = id
        }
    }


}