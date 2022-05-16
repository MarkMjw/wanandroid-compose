package com.compose.wanandroid

import android.app.Application
import com.compose.wanandroid.data.remote.apiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(apiModule))
        }
    }
}