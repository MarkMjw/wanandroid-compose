package com.compose.wanandroid.data.repository

import androidx.annotation.WorkerThread
import com.compose.wanandroid.data.local.model.History
import com.compose.wanandroid.data.local.dao.HistoryDao

class HistoryRepository(private val dao: HistoryDao) {

    companion object {
        const val READ_RECORD_MAX_COUNT = 1000
    }

    @WorkerThread
    suspend fun add(
        link: String,
        title: String,
        percent: Float = 0f
    ) {
        val time = System.currentTimeMillis()
        val model = History(
            link, title, time, time,
            (percent * History.MAX_PERCENT).toInt()
        )
        dao.insert(model)
        deleteIfMaxCount()
    }

    suspend fun updatePercent(
        link: String,
        percent: Float,
        lastTime: Long
    ) {
        val p = (percent.coerceIn(0f, 1f) * History.MAX_PERCENT).toInt()
        dao.updatePercent(link, p, lastTime)
    }

    suspend fun delete(link: String) {
        dao.delete(link)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    private suspend fun deleteIfMaxCount() {
        dao.deleteIfMaxCount(READ_RECORD_MAX_COUNT)
    }

    suspend fun find(from: Int, count: Int): List<History> {
        return dao.query(from, count)
    }

    suspend fun findByLinks(link: List<String>): List<History> {
        return dao.queryByLinks(link)
    }
}