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
//    backRoute: String? = null,
//    isLaunchSingleTop: Boolean = true,
//    isRestoreState: Boolean = true,
    vararg args: Any,
) {
    val params = if (args.isEmpty()) {
        route
    } else {
        args.joinToString("/") {
            if (it is Parcelable) {
                Uri.encode(it.toJson())
            } else {
                "$it"
            }
        }
    }
    val realRoute = "$route/$params"
    Logger.i("Navigate", realRoute)
    navigate(realRoute) {
//        if (backRoute != null) {
//            popUpTo(backRoute) { saveState = true }
//        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.back() {
    navigateUp()
}