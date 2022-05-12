package com.compose.wanandroid.data.remote

import com.compose.wanandroid.data.model.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import retrofit2.http.*

interface Api {
    companion object {
        const val HOST = "https://www.wanandroid.com/"
    }

    @GET("banner/json")
    suspend fun banners(): Response<MutableList<Banner>>

    @GET("article/top/json")
    suspend fun topArticles(): Response<MutableList<Article>>

    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page: Int): ListResponse<Article>

    @GET("wenda/list/{page}/json")
    suspend fun wendaList(@Path("page") page: Int): ListResponse<Article>

    @GET("tree/json")
    suspend fun structList(): Response<MutableList<Struct>>

    @GET("navi/json")
    suspend fun navigationList(): Response<MutableList<Navigate>>

    @GET("article/list/{page}/json")
    suspend fun structArticles(@Path("page") page: Int, @Query("cid") cid: Int): ListResponse<Article>

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): Response<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Response<UserInfo>

    @GET("user/logout/json")
    suspend fun logout(): Response<String>

    @GET("user/lg/userinfo/json")
    fun userInfo(): Flow<Response<UserResponse>>
}

object ApiService : KoinComponent {
    val api: Api by inject { parametersOf(Api.HOST) }
}