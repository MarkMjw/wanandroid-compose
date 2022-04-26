package com.compose.wanandroid.logic

import com.compose.wanandroid.BuildConfig
import com.compose.wanandroid.data.remote.Api
import com.compose.wanandroid.logic.ssl.SSLContextFactory
import com.compose.wanandroid.logic.ssl.SimpleX509TrustManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single { createOkHttpClient() }
    single { createRetrofitBuilder(get()) }
    factory { (baseUrl: String) -> createApi(get(), baseUrl) }
}

val gson: Gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .disableHtmlEscaping()
    .create()

private fun createApi(builder: Retrofit.Builder, baseUrl: String) = builder.baseUrl(baseUrl).build().create(Api::class.java)

private fun createRetrofitBuilder(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))

private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().apply {
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(20, TimeUnit.SECONDS)
        writeTimeout(20, TimeUnit.SECONDS)
        retryOnConnectionFailure(false)
        addNetworkInterceptor(LogInterceptor().apply {
            level = if (BuildConfig.DEBUG) LogInterceptor.Level.BASIC else LogInterceptor.Level.NONE
        })

        try {
            connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            sslSocketFactory(SSLContextFactory.makeContext().socketFactory, SimpleX509TrustManager())
        } catch (e: Exception) {
            Logger.e(e)
        }
    }.build()
}