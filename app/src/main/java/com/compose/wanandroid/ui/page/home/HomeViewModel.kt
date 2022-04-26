package com.compose.wanandroid.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.articleList(it) }
    }

}