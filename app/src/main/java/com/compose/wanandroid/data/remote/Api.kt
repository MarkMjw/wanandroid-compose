package com.compose.wanandroid.data.remote

import com.compose.wanandroid.data.model.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object {
        const val HOST = "https://www.wanandroid.com"
    }

    @GET("/banner/json")
    suspend fun banners(): Response<MutableList<Banner>>

    @GET("/article/top/json")
    suspend fun topArticles(): Response<MutableList<Article>>

    @GET("/article/list/{page}/json")
    suspend fun articles(@Path("page") page: Int): ListResponse<Article>

    @GET("/wenda/list/{page}/json")
    suspend fun wendaList(@Path("page") page: Int): ListResponse<Article>

    @GET("/tree/json")
    suspend fun structList(): Response<MutableList<Struct>>

    @GET("/navi/json")
    suspend fun navigationList(): Response<MutableList<Navigate>>

    @GET("/article/list/{page}/json")
    suspend fun structArticles(@Path("page") page: Int, @Query("cid") cid: Int): ListResponse<Article>
}

object ApiService : KoinComponent {
    val api: Api by inject { parametersOf(Api.HOST) }
}