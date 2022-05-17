package com.compose.wanandroid.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.compose.wanandroid.data.model.ListResponse
import com.compose.wanandroid.data.remote.HttpResult
import kotlinx.coroutines.flow.Flow

val defaultPage = PagingConfig(
    pageSize = 20,
    initialLoadSize = 20,
    prefetchDistance = 1,
    maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
    enablePlaceholders = false
)

fun <K : Any, V : Any> ViewModel.pageFlow(
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

fun <T : Any> ViewModel.pageLoading(
    config: PagingConfig = defaultPage,
    initialKey: Int = 0,
    block: suspend (page: Int) -> ListResponse<T>
): Flow<PagingData<T>> {
    return pageFlow(config, initialKey) {
        val page = it.key ?: 0
        val response = try {
            HttpResult.Success(block(page))
        } catch (e: Exception) {
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

fun <T : Any> ViewModel.pageLoadingLocal(
    config: PagingConfig = defaultPage,
    initialKey: Int = 0,
    block: suspend (page: Int) -> List<T>
): Flow<PagingData<T>> {
    return pageFlow(config, initialKey) {
        val page = it.key ?: 0
        try {
            val data = block(page)
            val hasNext = data.size >= it.loadSize
            PagingSource.LoadResult.Page(
                data = data,
                prevKey = if (page - it.loadSize > 0) page - it.loadSize else null,
                nextKey = if (hasNext) page + data.size else null
            )
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}
