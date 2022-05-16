package com.compose.wanandroid.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey val link: String,
    @ColumnInfo val title: String,
    @ColumnInfo val time: Long
)