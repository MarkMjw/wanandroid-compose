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
import com.compose.wanandroid.logic.pageLoading
import com.compose.wanandroid.data.repository.ArticleListRepository
import com.compose.wanandroid.data.repository.CollectRepository
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repo: ArticleListRepository
) : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        pageLoading { repo.getArticleList(it) }
    }

    var viewState by mutableStateOf(CategoryViewState(pagingData = pager))
        private set

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val collectRepo by lazy { CollectRepository() }

    fun dispatch(action: ViewAction) {
        when (action) {
            is CollectViewAction.Collect -> collect(action.article)
        }
    }

    private fun collect(article: Article) {
        viewModelScope.launch {
            _viewEvents.send(ProgressViewEvent(true))
            val result = if (!article.collect) collectRepo.collect(article) else collectRepo.unCollect(article)
            if (result.isFailure) {
                _viewEvents.send(SnackViewEvent("操作失败，请稍后重试~"))
            }
            _viewEvents.send(ProgressViewEvent(false))
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