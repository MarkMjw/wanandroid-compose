package com.compose.wanandroid.ui.page.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.HttpResult
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.UserStore
import com.compose.wanandroid.logic.darkMode
import com.compose.wanandroid.ui.page.login.LoginViewEvent
import com.compose.wanandroid.ui.theme.Theme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    var viewState by mutableStateOf(SettingViewState())
        private set

    private val _viewEvents = Channel<SettingViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    init {
        UserStore.isLogin.onEach {
            viewState = viewState.copy(isLogin = it)
        }.launchIn(viewModelScope)
    }

    fun dispatch(action: SettingViewAction) {
        when (action) {
            is SettingViewAction.Logout -> logout()
            is SettingViewAction.Dark -> setDark(action.mode)
        }
    }

    private fun logout() {
        flow {
            emit(ApiService.api.logout())
        }.map {
            if (it.isSuccess) {
                it.data ?: throw Exception("the result of remote's request is null")
            } else {
                throw Exception(it.errorMsg)
            }
        }.onEach {
            viewState = viewState.copy(isLogin = false)
            UserStore.logout()
        }.catch {
            _viewEvents.send(SettingViewEvent.ErrorTip(it.message ?: "logout failed, please retry later."))
        }.launchIn(viewModelScope)
    }

    private fun setDark(mode: String) {
        viewState = viewState.copy(dark = mode)
        viewModelScope.launch {
            darkMode.update(mode)
        }
    }
}

data class SettingViewState(
    var isLogin: Boolean = false,
    var dark: String = "",
    val darkModes: List<Theme> = listOf(Theme.FollowSystem, Theme.Light, Theme.Dark)
)

sealed class SettingViewAction {
    object Logout : SettingViewAction()
    data class Dark(val mode: String) : SettingViewAction()
}

sealed class SettingViewEvent {
    data class ErrorTip(val message: String) : SettingViewEvent()
}