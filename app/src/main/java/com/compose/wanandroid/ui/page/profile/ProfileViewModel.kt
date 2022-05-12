package com.compose.wanandroid.ui.page.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.CoinInfo
import com.compose.wanandroid.data.model.UserInfo
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.UserStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class ProfileViewModel : ViewModel() {
    var viewState by mutableStateOf(ProfileViewState())
        private set

    private val _viewEvents = Channel<ProfileViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    init {
        UserStore.isLogin.onEach {
            viewState = viewState.copy(isLogin = it)
            if (it) {
                fetchUserInfo()
            }
        }.launchIn(viewModelScope)

        UserStore.userInfo.onEach {
            viewState = viewState.copy(userInfo = it)
        }.launchIn(viewModelScope)

        UserStore.coinInfo.onEach {
            viewState = viewState.copy(coinInfo = it)
        }.launchIn(viewModelScope)
    }

    private fun fetchUserInfo() {
        ApiService.api.userInfo().map {
            if (it.isSuccess) {
                it.data ?: throw Exception("the result of remote's request is null")
            } else {
                throw Exception(it.errorMsg)
            }
        }.onEach { res ->
            viewState = viewState.copy(userInfo = res.userInfo, coinInfo = res.coinInfo)
            if (res.userInfo != null && res.coinInfo != null) {
                UserStore.update(res.userInfo, res.coinInfo)
            }
        }.catch {
            Logger.w(it.message ?: "")
        }.launchIn(viewModelScope)
    }

    fun dispatch(action: ProfileViewAction) {
        when (action) {
            is ProfileViewAction.ClickBtn1 -> clickBtn1()
            is ProfileViewAction.ClickBtn2 -> clickBtn2()
            is ProfileViewAction.ClickBtn3 -> clickBtn3()
        }
    }

    private fun clickBtn1() {

    }

    private fun clickBtn2() {

    }

    private fun clickBtn3() {

    }
}

data class ProfileViewState(
    val isLogin: Boolean = false,
    val userInfo: UserInfo? = null,
    val coinInfo: CoinInfo? = null
)

sealed class ProfileViewEvent

sealed class ProfileViewAction {
    object ClickBtn1 : ProfileViewAction()
    object ClickBtn2 : ProfileViewAction()
    object ClickBtn3 : ProfileViewAction()
}