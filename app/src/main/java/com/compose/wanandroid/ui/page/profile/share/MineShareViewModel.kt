package com.compose.wanandroid.ui.page.profile.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.*
import com.compose.wanandroid.data.remote.*
import com.compose.wanandroid.data.repository.CollectRepository
import com.compose.wanandroid.logic.defaultPage
import com.compose.wanandroid.logic.pageFlow
import com.compose.wanandroid.ui.common.*
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MineShareViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage(initialKey = 1) {
            // 从page=1开始
            ApiService.api.mineShareArticles(it)
        }
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

    private fun loadPage(
        config: PagingConfig = defaultPage,
        initialKey: Int = 0,
        block: suspend (page: Int) -> Response<UserShareResponse>
    ): Flow<PagingData<Article>> {
        return pageFlow(config, initialKey) {
            val page = it.key ?: 0
            val response = try {
                HttpResult.Success(block(page))
            } catch (e: Exception) {
                HttpResult.Error(e)
            }

            when (response) {
                is HttpResult.Success -> {
                    val data = response.result.data?.shareArticles
                    if (data != null) {
                        val hasNext = data.datas.size >= it.loadSize || !data.over
                        PagingSource.LoadResult.Page(
                            data = data.datas,
                            prevKey = if (page - 1 > 0) page - 1 else null,
                            nextKey = if (hasNext) page + 1 else null
                        )
                    } else {
                        PagingSource.LoadResult.Invalid()
                    }
                }
                is HttpResult.Error -> PagingSource.LoadResult.Error(response.e)
            }
        }
    }
}