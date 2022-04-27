package com.compose.wanandroid.ui.page.question

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.data.remote.loadPage
import kotlinx.coroutines.flow.Flow

class QuestionViewModel : ViewModel() {

    val pager: Flow<PagingData<Article>> by lazy {
        loadPage { ApiService.api.wendaList(it) }
    }

}