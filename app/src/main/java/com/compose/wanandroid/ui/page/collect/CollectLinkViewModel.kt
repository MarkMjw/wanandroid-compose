package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.CollectLink
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*

class CollectLinkViewModel : ViewModel() {

    var viewState by mutableStateOf(CollectLinkViewState())
        private set

    init {
        dispatch(CollectLinkViewAction.FetchData)
    }

    fun dispatch(action: CollectLinkViewAction) {
        when (action) {
            is CollectLinkViewAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        ApiService.api.collectLinks()
            .map {
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
            }.launchIn(viewModelScope)
    }
}

data class CollectLinkViewState(
    val data: List<CollectLink> = emptyList(),
    val pageState: PageState = PageState.Loading,
    val listState: LazyListState = LazyListState()
) {
    val size = data.size
}

sealed class CollectLinkViewAction {
    object FetchData : CollectLinkViewAction()
}