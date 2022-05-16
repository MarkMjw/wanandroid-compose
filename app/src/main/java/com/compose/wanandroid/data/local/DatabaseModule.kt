package com.compose.wanandroid.data.local

import com.compose.wanandroid.data.repository.BookmarkRepository
import com.compose.wanandroid.data.repository.HistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.build(androidContext()) }
    single { HistoryRepository(get<AppDatabase>().historyDao()) }
    single { BookmarkRepository(get<AppDatabase>().bookmarkDao()) }
}