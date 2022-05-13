package com.compose.wanandroid.ui.page.category

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
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.ui.common.CollectViewAction
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val cid: Int
) : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.structArticles(it, cid) }
    }

    var viewState by mutableStateOf(CategoryViewState(pagingData = pager))
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: ViewAction) {
        when (action) {
            is CollectViewAction.Collect -> collect(action.article)
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
                        // TODO 刷新列表
//                        viewState.pagingData.collectIndexed { _, value ->
//                            value.filter { it.id == article.id }.map { it.collect = true }
//                        }
//                        viewState = viewState.copy(pagingData = viewState.pagingData)
                    } else {
                        _viewEvents.send(SnackViewEvent("收藏失败，请稍后重试~"))
                    }
                } catch (e: Throwable) {
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
                        // TODO 刷新列表
//                        viewState.pagingData.collectIndexed { _, value ->
//                            value.filter { it.id == article.id }.map { it.collect = false }
//                        }
//                        viewState = viewState.copy(pagingData = viewState.pagingData)
                    } else {
                        _viewEvents.send(SnackViewEvent("取消收藏失败，请稍后重试~"))
                    }
                } catch (e: Throwable) {
                    _viewEvents.send(SnackViewEvent("取消收藏失败，请稍后重试~"))
                }
            }
        }
    }
}

data class CategoryViewState(
    val pagingData: Flow<PagingData<Article>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
) {
    fun getPageState(pagingItems: LazyPagingItems<*>): PageState {
        val isEmpty = pagingItems.itemCount <= 0
        return when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> PageState.Success()
            is LoadState.NotLoading -> PageState.Success(isEmpty)
            is LoadState.Error -> if (isEmpty) PageState.Error() else PageState.Success()
        }
    }
}