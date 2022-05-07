package com.compose.wanandroid.logic

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavController
import com.google.gson.Gson

fun Parcelable.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}

fun NavController.navigate(
    route: String,
    args: Any? = null,
    backRoute: String? = null,
    isLaunchSingleTop: Boolean = true,
    isRestoreState: Boolean = true,
) {
    val realRoute = when (args) {
        null -> route
        is Parcelable -> "$route/${Uri.encode(args.toJson())}"
        else -> "$route/$args"
    }
    Logger.i("Navigate", realRoute)
    navigate(realRoute) {
        if (backRoute != null) {
            popUpTo(backRoute) { saveState = true }
        }
        launchSingleTop = isLaunchSingleTop
        restoreState = isRestoreState
    }
}

fun NavController.back() {
    navigateUp()
}