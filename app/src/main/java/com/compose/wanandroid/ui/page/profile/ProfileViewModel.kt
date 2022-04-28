package com.compose.wanandroid.ui.page.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ProfileViewModel : ViewModel() {
    var viewStates by mutableStateOf(TestViewState())
        private set

    private val _viewEvents = Channel<TestViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: TestViewAction) {
        when (action) {
            is TestViewAction.ClickBtn1 -> clickBtn1()
            is TestViewAction.ClickBtn2 -> clickBtn2()
            is TestViewAction.ClickBtn3 -> clickBtn3()
        }
    }

    private fun clickBtn1() {
        viewStates = viewStates.copy(state1 = "edit")
    }

    private fun clickBtn2() {
        viewStates = viewStates.copy(state2 = !viewStates.state2)
    }

    private fun clickBtn3() {
        viewStates = viewStates.copy(state3 = viewStates.state3 + 1)
    }
}

data class TestViewState(
    val state1: String = "state1",
    val state2: Boolean = false,
    val state3: Int = 0
)

sealed class TestViewEvent

sealed class TestViewAction {
    object ClickBtn1 : TestViewAction()
    object ClickBtn2 : TestViewAction()
    object ClickBtn3 : TestViewAction()
}