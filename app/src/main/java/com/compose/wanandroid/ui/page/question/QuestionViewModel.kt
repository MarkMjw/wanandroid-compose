package com.compose.wanandroid.ui.page.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.filter
import androidx.paging.map
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.ui.common.CollectViewAction
import com.compose.wanandroid.ui.common.SnackViewEvent
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class QuestionViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.wendaList(it) }
    }

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun getPageState(pagingItems: LazyPagingItems<*>): PageState {
        val isEmpty = pagingItems.itemCount <= 0
        return when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> PageState.Success()
            is LoadState.NotLoading -> PageState.Success(isEmpty)
            is LoadState.Error -> if (isEmpty) PageState.Error() else PageState.Success()
        }
    }

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
                        pager.collectIndexed { _, value ->
                            value.filter { it.id == article.id }.map { it.collect = true }
                        }
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
                        pager.collectIndexed { _, value ->
                            value.filter { it.id == article.id }.map { it.collect = false }
                        }
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