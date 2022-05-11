package com.compose.wanandroid.data.model

data class Response<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
) {
    val isSuccess: Boolean
        get() = errorCode == 0
}

data class ListWrapper<T>(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: MutableList<T>
)

typealias ListResponse<T> = Response<ListWrapper<T>>