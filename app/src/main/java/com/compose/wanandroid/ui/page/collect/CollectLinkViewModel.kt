package com.compose.wanandroid.ui.page.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.CollectLink
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class CollectLinkViewModel : ViewModel() {

    private var _links: SnapshotStateList<CollectLink>? = null

    var viewState by mutableStateOf(CollectLinkViewState())
        private set

    private val _viewEvents = Channel<CollectArticleViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    init {
        dispatch(CollectLinkViewAction.FetchData)
    }

    fun dispatch(action: CollectLinkViewAction) {
        when (action) {
            is CollectLinkViewAction.UnCollect -> unCollect(action.link)
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

sealed class CollectLinkViewAction {
    object FetchData : CollectLinkViewAction()
    data class UnCollect(val link: CollectLink) : CollectLinkViewAction()
}

sealed class CollectLinkViewEvent {
    data class Tip(val message: String) : CollectLinkViewEvent()
}