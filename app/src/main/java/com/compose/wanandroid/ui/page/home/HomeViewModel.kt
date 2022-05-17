package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Banner
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.repository.CollectRepository
import com.compose.wanandroid.logic.pageLoading
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        pageLoading { ApiService.api.articles(it) }
    }

    var viewState by mutableStateOf(HomeViewState(pagingData = pager))
        private set

    // LazyColumn结合SnapshotStateList，界面会自动观察数据变化
//    private var _tops = mutableStateListOf<Article>()

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val collectRepo by lazy { CollectRepository() }

    init {
        dispatch(RefreshViewAction.FetchData)
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is RefreshViewAction.FetchData -> fetchData()
            is RefreshViewAction.Refresh -> refresh()
            is CollectViewAction.Collect -> collect(action.article)
        }
    }

    private fun refresh() {
        fetchData()
    }

    private fun fetchData() {
        val banners = ApiService.api.banners().map { it.data ?: emptyList() }
        val tops = ApiService.api.topArticles().map { it.data ?: emptyList() }
        viewModelScope.launch {
            banners.zip(tops) { banners, tops ->
//                _tops = tops.toMutableStateList()
                viewState = viewState.copy(banners = banners, tops = tops, isRefreshing = false)
            }.onStart {
                viewState = viewState.copy(isRefreshing = true)
            }.catch {
                viewState = viewState.copy(isRefreshing = false)
            }.collect()
        }
    }

    private fun collect(article: Article) {
        viewModelScope.launch {
            _viewEvents.send(ProgressViewEvent(true))
            val result = if (!article.collect) collectRepo.collect(article) else collectRepo.unCollect(article)
            if (result.isSuccess) {
//                val index = _tops.indexOf(article)
//                if (index >= 0) {
//                    _tops.removeAt(index)
//                    _tops.add(index, article)
//                }
            } else {
                _viewEvents.send(SnackViewEvent("操作失败，请稍后重试~"))
            }
            _viewEvents.send(ProgressViewEvent(false))
        }
    }
}

data class HomeViewState(
    val banners: List<Banner> = emptyList(),
    val tops: List<Article> = emptyList(),
    val pagingData: Flow<PagingData<Article>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState()
) {
    fun getPageState(pagingItems: LazyPagingItems<*>): PageState {
        val isEmpty = tops.isEmpty() && banners.isEmpty() && pagingItems.itemCount <= 0
        return when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> PageState.Success()
            is LoadState.NotLoading -> PageState.Success(isEmpty)
            is LoadState.Error -> if (isEmpty) PageState.Error() else PageState.Success()
        }
    }
}