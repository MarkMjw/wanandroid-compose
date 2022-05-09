package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Banner
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.articles(it) }
    }

    var viewState by mutableStateOf(HomeViewState(pagingData = pager))
        private set

    init {
        dispatch(HomeViewAction.FetchData)
    }

    fun dispatch(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.FetchData -> fetchData()
            is HomeViewAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        fetchData()
    }

    private fun fetchData() {
        val banners = flow {
            emit(ApiService.api.banners())
        }.map { it.data ?: emptyList() }

        val tops = flow {
            emit(ApiService.api.topArticles())
        }.map { it.data ?: emptyList() }

        viewModelScope.launch {
            banners.zip(tops) { banners, tops ->
                viewState = viewState.copy(banners = banners, tops = tops, isRefreshing = false)
            }.onStart {
                viewState = viewState.copy(isRefreshing = true)
            }.catch {
                viewState = viewState.copy(isRefreshing = false)
            }.collect()
        }
    }
}

data class HomeViewState(
    val banners: List<Banner> = emptyList(),
    val tops: List<Article> = emptyList(),
    val pagingData: Flow<PagingData<Article>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
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

sealed class HomeViewAction {
    object FetchData : HomeViewAction()
    object Refresh : HomeViewAction()
}