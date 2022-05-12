package com.compose.wanandroid.ui.page.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.logic.UserStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var viewState by mutableStateOf(LoginViewState())
        private set

    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> if (action.isRegister) register() else login()
            is LoginViewAction.UpdateAccount -> viewState = viewState.copy(account = action.account)
            is LoginViewAction.UpdatePassword -> viewState = viewState.copy(password = action.password)
        }
    }

    private fun login() {
        viewModelScope.launch {
            try {
                _viewEvents.send(LoginViewEvent.Progress(true, "登录中..."))
                val res = ApiService.api.login(viewState.account.trim(), viewState.password.trim())
                if (res.isSuccess) {
                    _viewEvents.send(LoginViewEvent.Progress(false))
                    UserStore.login(res.data ?: throw Exception("登录失败，请稍后重试~"))
                    _viewEvents.send(LoginViewEvent.Back)
                } else {
                    throw Exception(res.errorMsg)
                }
            } catch (e: Throwable) {
                _viewEvents.send(LoginViewEvent.ErrorTip(e.message ?: "登录失败，请稍后重试~"))
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            try {
                _viewEvents.send(LoginViewEvent.Progress(true, "注册中..."))
                val res = ApiService.api.register(viewState.account.trim(), viewState.password.trim(), viewState.password.trim())
                if (res.isSuccess) {
                    _viewEvents.send(LoginViewEvent.Progress(false))
                    UserStore.login(res.data ?: throw Exception("注册失败，请稍后重试~"))
                    _viewEvents.send(LoginViewEvent.Back)
                } else {
                    throw Exception(res.errorMsg)
                }
            } catch (e: Throwable) {
                _viewEvents.send(LoginViewEvent.ErrorTip(e.message ?: "注册失败，请稍后重试~"))
            }
        }
    }
}

data class LoginViewState(
    var account: String = "",
    var password: String = "",
)

sealed class LoginViewAction {
    data class Login(val isRegister: Boolean) : LoginViewAction()
    data class UpdateAccount(val account: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
}

sealed class LoginViewEvent {
    object Back : LoginViewEvent()
    data class ErrorTip(val message: String) : LoginViewEvent()
    data class Progress(val show: Boolean, val message: String = "") : LoginViewEvent()
}