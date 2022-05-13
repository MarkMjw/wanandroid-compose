package com.compose.wanandroid.ui.page.category

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.data.repository.CategoryArticleListRepository
import com.compose.wanandroid.data.repository.ProjectArticleListRepository

class CategoryListViewModel : ViewModel() {

    private val viewModels: ArrayMap<Int, CategoryViewModel> = ArrayMap()

    fun getChildViewModel(struct: Struct): CategoryViewModel {
        val cid = struct.id
        return viewModels.getOrElse(cid) {
            val repo = if (struct.type == Struct.TYPE_PROJECT) {
                ProjectArticleListRepository(cid)
            } else {
                CategoryArticleListRepository(cid)
            }
            CategoryViewModel(repo).apply {
                viewModels[cid] = this
            }
        }
    }
}