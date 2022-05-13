package com.compose.wanandroid.ui.page.category

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel

class CategoryListViewModel : ViewModel() {

    private val viewModels: ArrayMap<Int, CategoryViewModel> = ArrayMap()

    fun getChildViewModel(cid: Int): CategoryViewModel {
        return viewModels.getOrElse(cid) {
            CategoryViewModel(cid).apply {
                viewModels[cid] = this
            }
        }
    }
}