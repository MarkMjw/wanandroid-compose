package com.compose.wanandroid.data.remote

sealed class HttpResult<out T> {
    data class Success<T>(val result: T) : HttpResult<T>()
    data class Error(val e: Exception) : HttpResult<Nothing>()
}
