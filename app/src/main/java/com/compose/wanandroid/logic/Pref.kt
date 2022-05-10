package com.compose.wanandroid.logic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.properties.ReadOnlyProperty

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("setting")

abstract class PreferenceItem<T>(flow: Flow<T>) : Flow<T> by flow {
    abstract suspend fun update(value: T?)
}

operator fun <T> DataStore<Preferences>.invoke(
    buildKey: (name: String) -> Preferences.Key<T>,
    defaultValue: T,
) = ReadOnlyProperty<Any?, PreferenceItem<T>> { _, property ->
    val key = buildKey(property.name)
    object : PreferenceItem<T>(data.map { it[key] ?: defaultValue }) {
        override suspend fun update(value: T?) {
            edit { pref ->
                if (value == null) {
                    pref -= key
                } else {
                    pref[key] = value
                }
            }
        }
    }
}

object Pref : KoinComponent {

    private val dataStore: DataStore<Preferences> by lazy {
        val context: Context by inject()
        context.dataStore
    }

    val darkMode: PreferenceItem<String> by dataStore(::stringPreferencesKey, "")

//    fun getString(key: String, default: String = ""): Flow<String> {
//        return dataStore.data.map { it[stringPreferencesKey(key)] ?: default }
//    }
//
//    fun getDouble(key: String, default: Double = 0.0): Flow<Double> {
//        return dataStore.data.map { it[doublePreferencesKey(key)] ?: default }
//    }
//
//    fun getFloat(key: String, default: Float = 0.0f): Flow<Float> {
//        return dataStore.data.map { it[floatPreferencesKey(key)] ?: default }
//    }
//
//    fun getLong(key: String, default: Long = 0L): Flow<Long> {
//        return dataStore.data.map { it[longPreferencesKey(key)] ?: default }
//    }
//
//    fun getInt(key: String, default: Int = 0): Flow<Int> {
//        return dataStore.data.map { it[intPreferencesKey(key)] ?: default }
//    }
//
//    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean> {
//        return dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }
//    }
//
//    fun getSet(key: String): Flow<Set<String>> {
//        return dataStore.data.map { it[stringSetPreferencesKey(key)] ?: emptySet() }
//    }
//
//    suspend inline fun <reified T : Any> get(key: String, default: T): T {
//        return when (T::class) {
//            String::class -> dataStore.data.map {
//                it[stringPreferencesKey(key)] ?: default
//            }.first() as T
//
//            Double::class -> dataStore.data.map {
//                it[doublePreferencesKey(key)] ?: default
//            }.first() as T
//
//            Float::class -> dataStore.data.map {
//                it[floatPreferencesKey(key)] ?: default
//            }.first() as T
//
//            Long::class -> dataStore.data.map {
//                it[longPreferencesKey(key)] ?: default
//            }.first() as T
//
//            Int::class -> dataStore.data.map {
//                it[intPreferencesKey(key)] ?: default
//            }.first() as T
//
//            Boolean::class -> dataStore.data.map {
//                it[booleanPreferencesKey(key)] ?: default
//            }.first() as T
//
//            else -> throw IllegalArgumentException("This type can be read from DataStore")
//        }
//    }
//
//    suspend fun <T : Any> put(key: String, value: T) {
//        dataStore.edit { map ->
//            when (value) {
//                is String -> map[stringPreferencesKey(key)] = value
//                is Double -> map[doublePreferencesKey(key)] = value
//                is Float -> map[floatPreferencesKey(key)] = value
//                is Long -> map[longPreferencesKey(key)] = value
//                is Int -> map[intPreferencesKey(key)] = value
//                is Boolean -> map[booleanPreferencesKey(key)] = value
//                else -> throw IllegalArgumentException("This type can be saved into DataStore")
//            }
//        }
//    }
//
//    suspend fun putSet(key: String, value: Set<String>) {
//        dataStore.edit { map ->
//            map[stringSetPreferencesKey(key)] = value
//        }
//    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}