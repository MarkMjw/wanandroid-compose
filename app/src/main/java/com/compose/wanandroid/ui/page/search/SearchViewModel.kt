package com.compose.wanandroid.ui.page.search

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.logic.pageLoading
import com.compose.wanandroid.logic.searchHistory
import com.compose.wanandroid.ui.common.RefreshViewAction
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private var keyword: String = ""
    private val pager: Flow<PagingData<Article>> by lazy {
        pageLoading {
            if (keyword.isNotEmpty()) {
                ApiService.api.search(it, keyword)
            } else {
                throw Exception("Search key is empty.")
            }
        }
    }

    var viewState by mutableStateOf(SearchViewState(pagingData = pager))
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    init {
        dispatch(RefreshViewAction.FetchData)
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is RefreshViewAction.FetchData -> fetchData()
            is SearchViewAction.Search -> search(action.key)
            is SearchViewAction.ClearHistory -> clearHistory()
        }
    }

    private fun fetchData() {
        val hotKeys = ApiService.api.hotKey().map { it.data ?: emptyList() }
        val historyKeys = searchHistory.map { it.split(",") }
        viewModelScope.launch {
            hotKeys.zip(historyKeys) { hot, history ->
                viewState = viewState.copy(hotKeys = hot.map { it.name }, historyKeys = history.filter { it.isNotEmpty() })
            }.collect()
        }
    }

    private fun search(key: String) {
        viewModelScope.launch {
            val keyword = key.trim()
            if (keyword.isEmpty()) {
                _viewEvents.send(SnackViewEvent("请输入有效关键字~"))
            } else {
                this@SearchViewModel.keyword = keyword
                viewState = viewState.copy(historyKeys = (viewState.historyKeys + keyword).distinct())
                _viewEvents.send(SearchViewEvent.Searching)

                val history = viewState.historyKeys.joinToString(",")
                searchHistory.update(history)
            }
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            viewState = viewState.copy(historyKeys = emptyList())
            searchHistory.update("")
        }
    }
}

data class SearchViewState(
    val hotKeys: List<String> = emptyList(),
    val historyKeys: List<String> = emptyList(),
    val pagingData: Flow<PagingData<Article>>,
    val listState: LazyListState = LazyListState(),
)

sealed class SearchViewAction : ViewAction {
    object ClearHistory : SearchViewAction()
    data class Search(val key: String) : SearchViewAction()
}

sealed class SearchViewEvent : ViewEvent {
    object Searching : SearchViewEvent()
}