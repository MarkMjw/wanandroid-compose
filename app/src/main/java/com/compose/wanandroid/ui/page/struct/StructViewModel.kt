package com.compose.wanandroid.ui.page.struct

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.wanandroid.data.model.Struct
import com.compose.wanandroid.data.remote.ApiService
import com.compose.wanandroid.logic.zips
import com.compose.wanandroid.ui.widget.PageState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StructViewModel : ViewModel() {

    var viewState by mutableStateOf(StructViewState())
        private set

    init {
        dispatch(StructViewAction.FetchData)
    }

    fun dispatch(action: StructViewAction) {
        when (action) {
            is StructViewAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        val accounts = flow {
            emit(ApiService.api.wxAccountList())
        }.map { it.data ?: emptyList() }

        val projects = flow {
            emit(ApiService.api.projectList())
        }.map { it.data ?: emptyList() }

        val structs = flow {
            emit(ApiService.api.structList())
        }.map { it.data ?: emptyList() }

        viewModelScope.launch {
            structs.zips(accounts, projects) { structs, accounts, projects ->
                val list = mutableListOf<Struct>()
                if (accounts.isNotEmpty()) {
                    // 公众号当做一个特殊的类型
                    list.add(Struct(name = "公众号", children = accounts.toMutableList(), type = Struct.TYPE_ACCOUNT))
                }
                if (projects.isNotEmpty()) {
                    // 项目当做一个特殊的类型
                    list.add(Struct(name = "项目", children = projects.toMutableList(), type = Struct.TYPE_PROJECT))
                }
                list.addAll(structs)
                viewState = viewState.copy(
                    data = list,
                    pageState = PageState.Success(list.isEmpty())
                )
            }.onStart {
                viewState = viewState.copy(pageState = PageState.Loading)
            }.catch {
                viewState = viewState.copy(pageState = PageState.Error(it))
            }.collect()
        }
    }
}

data class StructViewState(
    val data: List<Struct> = emptyList(),
    val pageState: PageState = PageState.Loading,
    val listState: LazyListState = LazyListState()
) {
    val size = data.size
}

sealed class StructViewAction {
    object FetchData : StructViewAction()
}