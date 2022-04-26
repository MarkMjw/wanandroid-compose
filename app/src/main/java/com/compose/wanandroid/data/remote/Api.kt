package com.compose.wanandroid.data.remote

import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.ListWrapper
import com.compose.wanandroid.data.model.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    companion object {
        const val HOST = "https://www.wanandroid.com"
    }

    @GET("/article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): Response<ListWrapper<Article>>
}

object ApiService : KoinComponent {
    val api: Api by inject { parametersOf(Api.HOST) }
}