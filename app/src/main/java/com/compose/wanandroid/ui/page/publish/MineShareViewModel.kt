package com.compose.wanandroid.ui.page.publish

import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.*
import com.compose.wanandroid.data.remote.*
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.CollectViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MineShareViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage(initialKey = 1) {
            // 从page=1开始
            ApiService.api.mineShareArticles(it)
        }
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
            is CollectViewAction.UnCollect -> unCollect(action.article)
        }
    }

    private fun unCollect(article: Article) {
        // TODO 取消收藏
    }

    private fun loadPage(
        config: PagingConfig = defaultPage,
        initialKey: Int = 0,
        block: suspend (page: Int) -> Response<UserShareResponse>
    ): Flow<PagingData<Article>> {
        return page(config, initialKey) {
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