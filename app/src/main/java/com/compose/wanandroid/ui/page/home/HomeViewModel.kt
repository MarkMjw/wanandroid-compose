package com.compose.wanandroid.ui.page.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.Banner
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.articles(it) }
    }

    var viewState by mutableStateOf(HomeViewState(pagingData = pager))
        private set

    private var _tops = mutableStateListOf<Article>()

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

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
        val banners = flow {
            emit(ApiService.api.banners())
        }.map { it.data ?: emptyList() }

        val tops = flow {
            emit(ApiService.api.topArticles())
        }.map { it.data ?: emptyList() }

        viewModelScope.launch {
            banners.zip(tops) { banners, tops ->
                _tops = tops.toMutableStateList()
                viewState = viewState.copy(banners = banners, tops = _tops, isRefreshing = false)
            }.onStart {
                viewState = viewState.copy(isRefreshing = true)
            }.catch {
                viewState = viewState.copy(isRefreshing = false)
            }.collect()
        }
    }

    private fun collect(article: Article) {
        viewModelScope.launch {
            if (!article.collect) {
                try {
                    val result = if (article.author.isEmpty()) {
                        ApiService.api.collectLink(article.title, article.link)
                    } else {
                        ApiService.api.collectArticle(article.id)
                    }
                    if (result.isSuccess) {
                        val index = _tops.indexOf(article)
                        if (index >= 0) {
                            article.collect = true
                            Logger.w("mjw", "${article.title} -> ${article.collect}")
                            _tops[index] = article
                        }
                        _viewEvents.send(SnackViewEvent("恭喜你：收藏成功~"))

//                        _tops.filter { it.id == article.id }.forEach {
//                            it.collect = true
//                            Logger.w("mjw", "${it.title} -> ${it.collect}")
//                        }

//                        viewState.tops.filter { it.id == article.id }.map { it.collect = true }
//                        viewState.pagingData.collectIndexed { _, value ->
//                            value.filter { it.id == article.id }.map { it.collect = true }
//                        }
//                        viewState = viewState.copy(tops = viewState.tops, pagingData = viewState.pagingData)
                    } else {
                        _viewEvents.send(SnackViewEvent("收藏失败，请稍后重试~"))
                    }
                } catch (e: Throwable) {
                    Logger.e("mjw", e)
                    _viewEvents.send(SnackViewEvent("收藏失败，请稍后重试~"))
                }
            } else {
                try {
                    val result = if (article.author.isEmpty()) {
                        ApiService.api.unCollectLink(article.id)
                    } else {
                        ApiService.api.unCollectArticle(article.id)
                    }
                    if (result.isSuccess) {
                        val index = _tops.indexOf(article)
                        if (index >= 0) {
                            article.collect = false
                            Logger.i("mjw", "${article.title} -> ${article.collect}")
                            _tops[index] = article
                        }

                        _viewEvents.send(SnackViewEvent("恭喜你：取消收藏成功~"))

//                        _tops.filter { it.id == article.id }.forEach {
//                            it.collect = false
//                            Logger.i("mjw", "${it.title} -> ${it.collect}")
//                        }

//                        viewState.tops.filter { it.id == article.id }.map { it.collect = false }
//                        viewState.pagingData.collectIndexed { _, value ->
//                            value.filter { it.id == article.id }.map { it.collect = false }
//                        }
//                        viewState = viewState.copy(tops = viewState.tops, pagingData = viewState.pagingData)
                    } else {
                        _viewEvents.send(SnackViewEvent("取消收藏失败，请稍后重试~"))
                    }
                } catch (e: Throwable) {
                    Logger.e("mjw", e)
                    _viewEvents.send(SnackViewEvent("取消收藏失败，请稍后重试~"))
                }
            }
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