package com.compose.wanandroid.ui.common

import com.compose.wanandroid.data.model.Article

interface ViewAction

sealed class RefreshViewAction : ViewAction {
    object FetchData : RefreshViewAction()
    object Refresh : RefreshViewAction()
}

sealed class CollectViewAction : ViewAction {
    data class Collect(val article: Article) : CollectViewAction()
    data class UnCollect(val article: Article) : CollectViewAction()
}