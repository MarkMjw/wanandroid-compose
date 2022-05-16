package com.compose.wanandroid.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.compose.wanandroid.data.local.dao.BookmarkDao
import com.compose.wanandroid.data.local.dao.HistoryDao
import com.compose.wanandroid.data.local.model.Bookmark
import com.compose.wanandroid.data.local.model.History

@Database(
    entities = [
        Bookmark::class,
        History::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val NAME = "wanandroid.db"

        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, NAME).build()
        }
    }

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun historyDao(): HistoryDao
}