package com.app.emilockerapp.datalayer.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user-prefs")

    companion object {
        val LOCK_TASK_MODE = booleanPreferencesKey("is_lock")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val isDarkMode : Flow<Boolean> = context.dataStore.data.map { preferences->
        preferences[DARK_MODE_KEY]?:false
    }

    suspend fun setDarkMode(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { preferences->
            preferences[DARK_MODE_KEY] = isDarkMode
        }
    }
}