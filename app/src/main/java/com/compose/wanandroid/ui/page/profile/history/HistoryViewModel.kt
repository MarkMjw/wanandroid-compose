package com.compose.wanandroid.ui.page.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.local.model.History
import com.compose.wanandroid.data.repository.HistoryRepository
import com.compose.wanandroid.logic.pageLoadingLocal
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : ViewModel(), KoinComponent {

    private val repo: HistoryRepository by inject()

    val pager: Flow<PagingData<History>> by lazy {
        pageLoadingLocal {
            repo.find(it, 20)
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
            is HistoryViewAction.Delete -> delete(action.history)
        }
    }

    private fun delete(history: History) {
        viewModelScope.launch {
            repo.delete(history.link)
        }
    }
}

sealed class HistoryViewAction : ViewAction {
    data class Delete(val history: History) : HistoryViewAction()
}