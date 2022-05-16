package com.compose.wanandroid.ui.page.profile.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.CollectLink
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.RefreshViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class CollectLinkViewModel : ViewModel() {

    private var _links: SnapshotStateList<CollectLink>? = null

    var viewState by mutableStateOf(CollectLinkViewState())
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

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
                val links = it.toMutableStateList().apply { _links = this }
                viewState = viewState.copy(
                    data = links,
                    pageState = PageState.Success(it.isEmpty())
                )
            }.catch {
                viewState = viewState.copy(pageState = PageState.Error(it))
            }.launchIn(viewModelScope)
    }

    private fun unCollect(link: CollectLink) {
        // TODO 取消收藏
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