package com.compose.wanandroid.data.repository

import com.compose.wanandroid.data.model.Article
import com.compose.wanandroid.data.remote.ApiService

class CollectRepository {

    suspend fun collect(article: Article): Result<Unit> {
        return try {
            val result = if (article.author.isEmpty()) {
                ApiService.api.collectLink(article.title, article.link)
            } else {
                ApiService.api.collectArticle(article.id)
            }
            if (result.isSuccess) {
                article.collect = true
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.errorMsg))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun unCollect(article: Article): Result<Unit> {
        return try {
            val result = if (article.author.isEmpty()) {
                ApiService.api.unCollectLink(article.id)
            } else {
                ApiService.api.unCollectArticle(article.id)
            }
            if (result.isSuccess) {
                article.collect = false
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.errorMsg))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun unCollectMine(article: Article): Result<Unit> {
        return try {
            val result = if (article.author.isEmpty()) {
                ApiService.api.unCollectLink(article.id)
            } else {
                ApiService.api.unCollectArticle(article.id, article.originId)
            }
            if (result.isSuccess) {
                article.collect = false
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.errorMsg))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun unCollectLink(id: Int): Result<Unit> {
        return try {
            val result = ApiService.api.unCollectLink(id)
            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.errorMsg))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}