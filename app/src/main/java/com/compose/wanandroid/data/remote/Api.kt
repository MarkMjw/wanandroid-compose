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
    fun banners(): Flow<Response<MutableList<Banner>>>

    @GET("article/top/json")
    fun topArticles(): Flow<Response<MutableList<Article>>>

    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page: Int): ListResponse<Article>

    @GET("user_article/list/{page}/json")
    suspend fun squareArticles(@Path("page") page: Int): ListResponse<Article>

    @GET("wenda/list/{page}/json")
    suspend fun wendaList(@Path("page") page: Int): ListResponse<Article>

    @GET("tree/json")
    fun structList(): Flow<Response<MutableList<Struct>>>

    @GET("navi/json")
    suspend fun navigationList(): Response<MutableList<Navigate>>

    @GET("project/tree/json")
    fun projectList(): Flow<Response<MutableList<Struct>>>

    @GET("wxarticle/chapters/json")
    fun wxAccountList(): Flow<Response<MutableList<Struct>>>

    @GET("article/list/{page}/json")
    suspend fun structArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ListResponse<Article>

    @GET("project/list/{page}/json")
    suspend fun projectArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ListResponse<Article>

    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun wxArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ListResponse<Article>

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<UserInfo>

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

    @GET("lg/collect/list/{page}/json")
    suspend fun collectArticles(@Path("page") page: Int): ListResponse<Article>

    @GET("lg/collect/usertools/json")
    fun collectLinks(): Flow<Response<MutableList<CollectLink>>>

    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): Response<Any>

    @FormUrlEncoded
    @POST("lg/collect/add/json")
    suspend fun collectArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): Response<Article>

    @FormUrlEncoded
    @POST("lg/collect/addtool/json")
    suspend fun collectLink(
        @Field("name") name: String,
        @Field("link") link: String
    ): Response<CollectLink>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: Int): Response<Any>

    @FormUrlEncoded
    @POST("lg/collect/deletetool/json")
    suspend fun unCollectLink(@Field("id") id: Int): Response<Any>

    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    suspend fun unCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): Response<Any>

    @FormUrlEncoded
    @POST("lg/collect/updatetool/json")
    suspend fun updateCollectLink(
        @Field("id") id: Int,
        @Field("name") name: String?,
        @Field("link") link: String?
    ): Response<CollectLink>

    @GET("hotkey/json")
    fun hotKey(): Flow<Response<MutableList<HotKey>>>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Field("k") key: String
    ): ListResponse<Article>

    @GET("user/lg/private_articles/{page}/json")
    suspend fun mineShareArticles(@Path("page") page: Int): Response<UserShareResponse>
}

object ApiService : KoinComponent {
    val api: Api by inject { parametersOf(Api.HOST) }
}