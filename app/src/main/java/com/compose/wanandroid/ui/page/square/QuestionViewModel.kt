package com.compose.wanandroid.ui.page.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.repository.CollectRepository
import com.compose.wanandroid.logic.pageLoading
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class QuestionViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        pageLoading { ApiService.api.wendaList(it) }
    }

    private val _viewEvents = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val collectRepo by lazy { CollectRepository() }

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
            _viewEvents.send(ProgressViewEvent(true))
            val result = if (!article.collect) collectRepo.collect(article) else collectRepo.unCollect(article)
            if (result.isFailure) {
                _viewEvents.send(SnackViewEvent("操作失败，请稍后重试~"))
            }
            _viewEvents.send(ProgressViewEvent(false))
        }
    }
}