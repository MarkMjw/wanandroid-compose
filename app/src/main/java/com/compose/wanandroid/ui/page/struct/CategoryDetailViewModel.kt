package com.compose.wanandroid.ui.page.struct

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.Flow

class CategoryDetailViewModel(
    private val cid: Int
) : ViewModel() {

    private val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.structArticles(it, cid) }
    }

    var viewState by mutableStateOf(CategoryDetailViewState(pagingData = pager))
        private set
}

data class CategoryDetailViewState(
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

sealed class CategoryDetailViewAction {
    object FetchData : CategoryDetailViewAction()
    object Refresh : CategoryDetailViewAction()
}