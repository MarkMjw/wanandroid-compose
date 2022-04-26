package com.compose.wanandroid.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.compose.wanandroid.data.model.ListWrapper
import com.compose.wanandroid.data.model.Response
import kotlinx.coroutines.flow.Flow

val defaultPage = PagingConfig(
    pageSize = 20,
    initialLoadSize = 20,
    prefetchDistance = 1,
    maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
    enablePlaceholders = false
)

fun <K : Any, V : Any> ViewModel.page(
    config: PagingConfig = defaultPage,
    initialKey: K? = null,
    block: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    return Pager(
        config = config,
        initialKey = initialKey
    ) {
        object : PagingSource<K, V>() {
            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                return block(params)
            }

            override fun getRefreshKey(state: PagingState<K, V>): K? {
                return initialKey
            }

        }
    }.flow.cachedIn(viewModelScope)
}

fun <T : Any> ViewModel.loadPage(
    config: PagingConfig = defaultPage,
    block: suspend (page: Int) -> Response<ListWrapper<T>>
): Flow<PagingData<T>> {
    return page(config, 0) {
        val page = it.key ?: 0
        val response = try {
            HttpResult.Success(block(page))
        } catch (e: Exception) {
//            val context = getApplication<App>()
//            if (context.isNetConnected()) {
//                "请求失败，请重试".toast(context)
//            } else {
//                "没有网络，请重试".toast(context)
//            }
            HttpResult.Error(e)
        }

        when (response) {
            is HttpResult.Success -> {
                val data = response.result.data
                if (data != null) {
                    val hasNext = data.datas.size >= it.loadSize || !data.over
                    PagingSource.LoadResult.Page(
                        data = data.datas,
                        prevKey = if (page - 1 > 0) page - 1 else null,
                        nextKey = if (hasNext) page + 1 else null
                    )
                } else {
                    PagingSource.LoadResult.Invalid()
                }
            }
            is HttpResult.Error -> PagingSource.LoadResult.Error(response.e)
        }
    }
}
