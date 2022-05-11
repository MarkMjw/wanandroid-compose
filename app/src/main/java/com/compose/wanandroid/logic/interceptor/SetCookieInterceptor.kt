package com.compose.wanandroid.logic.interceptor

import com.compose.wanandroid.logic.Logger
import com.compose.wanandroid.logic.Pref
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class SetCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val domain = request.url.host
        // 获取domain内的cookie
        if (domain.isNotEmpty()) {
            val cookie = runBlocking { Pref.get(domain, "") }
            if (cookie.isNotEmpty()) {
                Logger.i("Request", "Set cookie: $cookie")
                builder.addHeader("Cookie", cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}