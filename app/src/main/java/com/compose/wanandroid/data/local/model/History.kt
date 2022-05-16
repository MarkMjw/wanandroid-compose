package com.compose.wanandroid.data.local.model

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey val link: String,
    @ColumnInfo val title: String,
    @ColumnInfo val time: Long,
    @ColumnInfo val lastTime: Long,
    @ColumnInfo @IntRange(from = 0, to = 10000) val percent: Int,
) {
    companion object {
        const val MIN_PERCENT = 0
        const val MAX_PERCENT = 10000
    }

    val percentFloat: Float
        @FloatRange(from = 0.0, to = 1.0)
        get() = (percent.toFloat() / MAX_PERCENT.toFloat()).coerceIn(0f, 1f)
}