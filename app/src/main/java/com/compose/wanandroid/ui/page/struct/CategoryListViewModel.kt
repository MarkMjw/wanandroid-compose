package com.compose.wanandroid.ui.page.struct

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel

class CategoryListViewModel : ViewModel() {

    private val viewModels: ArrayMap<Int, CategoryDetailViewModel> = ArrayMap()

    fun getChildViewModel(cid: Int): CategoryDetailViewModel {
        return viewModels.getOrElse(cid) {
            CategoryDetailViewModel(cid).apply {
                viewModels[cid] = this
            }
        }
    }
}