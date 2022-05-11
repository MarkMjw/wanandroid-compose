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
            is LoginViewAction.Login -> login(action.isRegister)
            is LoginViewAction.UpdateAccount -> viewState = viewState.copy(account = action.account)
            is LoginViewAction.UpdatePassword -> viewState = viewState.copy(password = action.password)
        }
    }

    private fun login(isRegister: Boolean) {
        viewModelScope.launch {
            try {
                _viewEvents.send(LoginViewEvent.Progress(true, "登录中..."))
                val res = ApiService.api.login(viewState.account.trim(), viewState.password.trim())
                if (res.isSuccess) {
                    _viewEvents.send(LoginViewEvent.Progress(false))
                    UserStore.login(res.data ?: throw Exception("login failed, please retry later."))
                    _viewEvents.send(LoginViewEvent.Back)
                } else {
                    throw Exception(res.errorMsg)
                }
            } catch (e: Throwable) {
                _viewEvents.send(LoginViewEvent.ErrorTip(e.message ?: "login failed, please retry later."))
            }
        }
    }
}

data class LoginViewState(
    var account: String = "",
    var password: String = "",
    var passwordAgain: String = ""
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