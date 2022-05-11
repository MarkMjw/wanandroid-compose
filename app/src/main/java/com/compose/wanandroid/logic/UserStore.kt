package com.compose.wanandroid.logic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.compose.wanandroid.data.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

val Context.userStore: DataStore<Preferences> by preferencesDataStore("account")

object UserStore : KoinComponent {
    private val userStore: DataStore<Preferences> by lazy {
        val context: Context by inject()
        context.userStore
    }

    val isLogin: PreferenceItem<Boolean> by userStore(::booleanPreferencesKey, false, "is_login")

    private val user: PreferenceItem<String> by userStore(::stringPreferencesKey, "", "user_info")

    val userInfo: Flow<UserInfo?>
        get() = user.map { it.fromJson<UserInfo>() }

    suspend fun login(userInfo: UserInfo) {
        isLogin.update(true)
        user.update(userInfo.toJson())
    }

    suspend fun update(userInfo: UserInfo) {
        user.update(userInfo.toJson())
    }

    suspend fun logout() {
//        isLogin.update(false)
//        user.update("")
        clear()
    }

    suspend fun clear() {
        userStore.edit {
            it.clear()
        }
    }
}