package com.compose.wanandroid.ui.page.profile.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.CollectLink
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.repository.CollectRepository
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectLinkViewModel : ViewModel() {

    private var _links = mutableStateListOf<CollectLink>()

    var viewState by mutableStateOf(CollectLinkViewState())
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val collectRepo by lazy { CollectRepository() }

    init {
        dispatch(RefreshViewAction.FetchData)
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is CollectLinkViewAction.UnCollect -> unCollect(action.link)
            is RefreshViewAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        ApiService.api.collectLinks()
            .map {
                it.data ?: emptyList()
            }.onStart {
                viewState = viewState.copy(pageState = PageState.Loading)
            }.onEach {
                _links = it.toMutableStateList()
                viewState = viewState.copy(
                    data = _links,
                    pageState = PageState.Success(it.isEmpty())
                )
            }.catch {
                viewState = viewState.copy(pageState = PageState.Error(it))
            }.launchIn(viewModelScope)
    }

    private fun unCollect(link: CollectLink) {
        viewModelScope.launch {
            val result = collectRepo.unCollectLink(link.id)
            if (result.isSuccess) {
                _links.remove(link)
            } else {
                _viewEvents.send(SnackViewEvent("操作失败，请稍后重试~"))
            }
        }
    }
}

data class CollectLinkViewState(
    val data: List<CollectLink> = emptyList(),
    val pageState: PageState = PageState.Loading,
    val listState: LazyListState = LazyListState()
) {
    val size = data.size
}

sealed class CollectLinkViewAction : ViewAction {
    data class UnCollect(val link: CollectLink) : CollectLinkViewAction()
}