package com.compose.wanandroid.data.repository

import com.compose.wanandroid.data.local.model.Bookmark
import com.compose.wanandroid.data.local.dao.BookmarkDao
import com.compose.wanandroid.ui.page.main.Page

class BookmarkRepository(private val dao: BookmarkDao) {

    suspend fun add(link: String, title: String) {
        dao.insert(Bookmark(link, title, System.currentTimeMillis()))
    }

    suspend fun delete(link: String) {
        dao.delete(link)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun find(from: Int, count: Int) = dao.query(from, count)

    suspend fun findByLink(link: String): List<Bookmark> {
        return dao.query(link)
    }

    suspend fun findLately(count: Int) {
        dao.queryLately(count)
    }
}