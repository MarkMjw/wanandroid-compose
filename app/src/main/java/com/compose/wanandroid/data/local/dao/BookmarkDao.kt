package com.compose.wanandroid.data.local.dao

import androidx.room.*
import com.compose.wanandroid.data.local.model.Bookmark

@Dao
interface BookmarkDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg mode: Bookmark)

    @Transaction
    @Query("DELETE FROM bookmark WHERE link = :link")
    suspend fun delete(link: String)

    @Transaction
    @Query("DELETE FROM bookmark")
    suspend fun deleteAll()

    @Query("SELECT * FROM bookmark ORDER BY time DESC LIMIT (:offset), (:count)")
    suspend fun query(offset: Int, count: Int): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE link = :link")
    suspend fun query(link: String): List<Bookmark>

    @Query("SELECT * FROM bookmark ORDER BY time DESC LIMIT 0, (:count)")
    suspend fun queryLately(count: Int): List<Bookmark>
}