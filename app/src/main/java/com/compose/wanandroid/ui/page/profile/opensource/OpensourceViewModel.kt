package com.compose.wanandroid.ui.page.profile.opensource

import androidx.lifecycle.ViewModel

class OpensourceViewModel : ViewModel() {

    val data = listOf(
        OpensourceProject(
            "google/accompanist",
            "A collection of extension libraries for Jetpack Compose.",
            "https://github.com/google/accompanist"
        ),
        OpensourceProject(
            "google/gson",
            "A Java serialization/deserialization library to convert Java Objects into JSON and back.",
            "https://github.com/google/gson"
        ),
        OpensourceProject(
            "coil-kt/coil",
            "Image loading for Android backed by Kotlin Coroutines.",
            "https://github.com/coil-kt/coil"
        ),
        OpensourceProject(
            "InsertKoinIO/koin",
            "Koin - a pragmatic lightweight dependency injection framework for Kotlin.",
            "https://github.com/InsertKoinIO/koin"
        ),
        OpensourceProject(
            "square/okhttp",
            "Square’s meticulous HTTP client for the JVM, Android, and GraalVM.",
            "https://github.com/square/okhttp"
        ),
        OpensourceProject(
            "square/retrofit",
            "A type-safe HTTP client for Android and the JVM",
            "https://github.com/square/retrofit"
        ),
    )
}

data class OpensourceProject(
    val title: String,
    val desc: String,
    val url: String
)