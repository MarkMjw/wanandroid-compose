package com.compose.wanandroid.ui.page.profile.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.compose.wanandroid.data.local.model.Bookmark
import com.compose.wanandroid.data.repository.BookmarkRepository
import com.compose.wanandroid.logic.pageLoadingLocal
import com.compose.wanandroid.ui.common.ViewAction
import com.compose.wanandroid.ui.common.ViewEvent
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BookmarkViewModel : ViewModel(), KoinComponent {

    private val repo: BookmarkRepository by inject()

    val pager: Flow<PagingData<Bookmark>> by lazy {
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
            is BookmarkViewAction.Delete -> delete(action.bookmark)
        }
    }

    private fun delete(bookmark: Bookmark) {
        viewModelScope.launch {
            repo.delete(bookmark.link)
        }
    }
}

sealed class BookmarkViewAction : ViewAction {
    data class Delete(val bookmark: Bookmark) : BookmarkViewAction()
}