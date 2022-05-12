package com.compose.wanandroid.ui.page.collect

import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class CollectArticleViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage {
            ApiService.api.collectArticles(it).apply {
                data?.datas?.forEach { it.collect = true }
            }
        }
    }

    private val _viewEvents = Channel<CollectArticleViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun getPageState(pagingItems: LazyPagingItems<*>): PageState {
        val isEmpty = pagingItems.itemCount <= 0
        return when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> PageState.Success()
            is LoadState.NotLoading -> PageState.Success(isEmpty)
            is LoadState.Error -> if (isEmpty) PageState.Error() else PageState.Success()
        }
    }

    fun dispatch(action: CollectArticleViewAction) {
        when (action) {
            is CollectArticleViewAction.UnCollect -> unCollect(action.article)
        }
    }

    private fun unCollect(article: Article) {
        // TODO 取消收藏
    }
}

sealed class CollectArticleViewAction {
    data class UnCollect(val article: Article) : CollectArticleViewAction()
}

sealed class CollectArticleViewEvent {
    data class Tip(val message: String) : CollectArticleViewEvent()
}