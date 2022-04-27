package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Banner
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.articles(it) }
    }

    var viewState by mutableStateOf(HomeViewState(pagingData = pager))
        private set

    init {
        refresh()
    }

    fun refresh() {
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
//        viewModelScope.launch {
//            viewState = viewState.copy(isRefreshing = true)
//            val banners = async { ApiService.api.banners().data ?: emptyList() }
//            val tops = async { ApiService.api.topArticles().data ?: emptyList() }
//            viewState = viewState.copy(banners = banners.await(), tops = tops.await(), isRefreshing = false)
//        }
    }
}

data class HomeViewState(
    val banners: List<Banner> = emptyList(),
    val tops: List<Article> = emptyList(),
    val pagingData: Flow<PagingData<Article>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState()
)