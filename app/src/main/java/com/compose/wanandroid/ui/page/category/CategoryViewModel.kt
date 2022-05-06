package com.compose.wanandroid.ui.page.category

import androidx.lifecycle.ViewModel
import com.compose.wanandroid.ui.widget.TabText

class CategoryViewModel : ViewModel() {
    val titles by lazy {
        listOf(
            TabText(0, "体系"),
            TabText(1, "导航"),
        )
    }
}