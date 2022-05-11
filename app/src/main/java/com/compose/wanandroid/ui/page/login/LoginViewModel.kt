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
        flow {
            emit(ApiService.api.login(viewState.account.trim(), viewState.password.trim()))
        }.map {
            if (it.isSuccess) {
                it.data ?: throw Exception("the result of remote's request is null")
            } else {
                throw Exception(it.errorMsg)
            }
        }.onEach {
            UserStore.login(it)
            _viewEvents.send(LoginViewEvent.Back)
        }.catch {
            _viewEvents.send(LoginViewEvent.ErrorTip(it.message ?: "login failed, please retry later."))
        }.launchIn(viewModelScope)
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
}