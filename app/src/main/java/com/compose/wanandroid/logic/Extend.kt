@file:Suppress("Deprecation")

package com.compose.wanandroid.logic

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.util.regex.Pattern

fun Context.isNetConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun CharSequence.toast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    if (!isNullOrEmpty()) {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            Toast.makeText(context, this, duration).show()
        } else {
            Handler(context.mainLooper).post { Toast.makeText(context, this@toast, duration).show() }
        }
    }
}

fun String.removeAllBank(count: Int): String {
    var s = ""
    if (isNotEmpty()) {
        val p = Pattern.compile("\\s{$count,}|\t|\r|\n");
        val m = p.matcher(this)
        s = m.replaceAll(" ");
    }
    return s
}