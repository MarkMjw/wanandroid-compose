package com.compose.wanandroid.data.repository

import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.model.ListResponse
import com.compose.wanandroid.data.remote.ApiService

interface ArticleListRepository {
    suspend fun getArticleList(page: Int): ListResponse<Article>
}

class CategoryArticleListRepository(private val cid: Int) : ArticleListRepository {
    override suspend fun getArticleList(page: Int): ListResponse<Article> = ApiService.api.structArticles(page, cid)
}

class ProjectArticleListRepository(private val cid: Int) : ArticleListRepository {
    override suspend fun getArticleList(page: Int): ListResponse<Article> = ApiService.api.projectArticles(page, cid)
}