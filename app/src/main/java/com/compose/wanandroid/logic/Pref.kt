package com.compose.wanandroid.logic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Pref(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("setting")
        val DARK_MODE = stringPreferencesKey("dark_mode")
    }

    val darkMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: ""
    }

    suspend fun setDarkMode(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = theme
        }
    }
}