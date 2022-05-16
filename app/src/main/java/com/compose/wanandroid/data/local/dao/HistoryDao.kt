package com.compose.wanandroid.data.local.dao

import androidx.room.*
import com.compose.wanandroid.data.local.model.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg mode: History)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg mode: History)

    @Transaction
    @Query("DELETE FROM history WHERE link = :link")
    suspend fun delete(link: String)

    @Transaction
    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Transaction
    @Query(
        "DELETE FROM history WHERE link NOT IN (SELECT link FROM history ORDER BY time DESC LIMIT 0, :maxCount)"
    )
    suspend fun deleteIfMaxCount(maxCount: Int)

    @Transaction
    @Query("UPDATE history SET lastTime = :lastTime, percent = :percent WHERE (link = :link AND percent < :percent)")
    suspend fun updatePercent(link: String, percent: Int, lastTime: Long)

    @Query("SELECT * FROM history ORDER BY time DESC LIMIT (:offset), (:count)")
    suspend fun query(offset: Int, count: Int): List<History>

    @Query("SELECT * FROM history WHERE link = :link")
    suspend fun queryByLink(link: String): History?

    @Query("SELECT * FROM history WHERE link in (:links)")
    suspend fun queryByLinks(links: List<String>): List<History>
}