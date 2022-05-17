package com.compose.wanandroid.ui.page.profile.coin

import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.model.*
import com.compose.wanandroid.data.remote.*
import com.compose.wanandroid.logic.pageLoading
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*

class CoinViewModel : ViewModel() {

    val pager: Flow<PagingData<Coin>> by lazy {
        pageLoading(initialKey = 1) { ApiService.api.coinList(it) }
    }

    fun getPageState(pagingItems: LazyPagingItems<*>): PageState {
        val isEmpty = pagingItems.itemCount <= 0
        return when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> PageState.Success()
            is LoadState.NotLoading -> PageState.Success(isEmpty)
            is LoadState.Error -> if (isEmpty) PageState.Error() else PageState.Success()
        }
    }
}