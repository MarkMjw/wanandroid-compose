package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.Navigate
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NavigateViewModel : ViewModel() {

    var viewState by mutableStateOf(NavigateViewState())
        private set

    init {
        dispatch(NavigateViewAction.FetchData)
    }

    fun dispatch(action: NavigateViewAction) {
        when (action) {
            is NavigateViewAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            flow {
                emit(ApiService.api.navigationList())
            }.map {
                it.data ?: emptyList()
            }.onStart {
                viewState = viewState.copy(pageState = PageState.Loading)
            }.onEach {
                viewState = viewState.copy(
                    data = it,
                    pageState = PageState.Success(it.isEmpty())
                )
            }.catch {
                viewState = viewState.copy(pageState = PageState.Error(it))
            }.collect()
        }
    }
}

data class NavigateViewState(
    val data: List<Navigate> = emptyList(),
    val pageState: PageState = PageState.Loading,
    val listState: LazyListState = LazyListState()
) {
    val size = data.size
}

sealed class NavigateViewAction {
    object FetchData : NavigateViewAction()
}