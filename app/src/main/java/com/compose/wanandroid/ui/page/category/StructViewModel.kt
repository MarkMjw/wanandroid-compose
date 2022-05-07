package com.compose.wanandroid.ui.page.category

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StructViewModel : ViewModel() {

    var viewState by mutableStateOf(StructViewState())
        private set

    init {
        dispatch(StructViewAction.FetchData)
    }

    fun dispatch(action: StructViewAction) {
        when (action) {
            is StructViewAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            flow {
                emit(ApiService.api.structList())
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

data class StructViewState(
    val data: List<Struct> = emptyList(),
    val pageState: PageState = PageState.Loading,
    val listState: LazyListState = LazyListState()
) {
    val size = data.size
}

sealed class StructViewAction {
    object FetchData : StructViewAction()
}