package com.compose.wanandroid.logic

import androidx.datastore.preferences.core.stringPreferencesKey

val darkMode: PreferenceItem<String> by Pref.dataStore(::stringPreferencesKey, "", "dark_mode")

val cookie: PreferenceItem<String> by Pref.dataStore(::stringPreferencesKey, "", "cookie")