package com.compose.wanandroid.ui.page.profile.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.logic.UserStore
import com.compose.wanandroid.logic.darkMode
import com.compose.wanandroid.ui.common.ProgressViewEvent
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.theme.Theme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    var viewState by mutableStateOf(SettingViewState())
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
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
        viewModelScope.launch {
            try {
                _viewEvents.send(ProgressViewEvent(true, "退出中..."))
                val res = ApiService.api.logout()
                if (res.isSuccess) {
                    _viewEvents.send(ProgressViewEvent(false))
                    viewState = viewState.copy(isLogin = false)
                    UserStore.logout()
                } else {
                    throw Exception(res.errorMsg)
                }
            } catch (e: Throwable) {
                _viewEvents.send(SnackViewEvent(e.message ?: "退出登录失败，请稍后重试~"))
            }
        }
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