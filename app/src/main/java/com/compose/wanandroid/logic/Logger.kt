package com.compose.wanandroid.logic

import android.util.Log

object Logger {

    /** 是否打开日志 */
    var enable = true

    private const val DEFAULT_TAG = "Logger"

    private const val TYPE_V = 0x01
    private const val TYPE_D = TYPE_V shl 1
    private const val TYPE_I = TYPE_D shl 1
    private const val TYPE_W = TYPE_I shl 1
    private const val TYPE_E = TYPE_W shl 1

    // *******************Log with ERROR*******************/
    fun e(msg: String) {
        log(DEFAULT_TAG, msg, TYPE_E)
    }

    fun e(e: Exception) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_E)
    }

    fun e(tr: Throwable) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_E)
    }

    fun e(tag: String, msg: String) {
        log(tag, msg, TYPE_E)
    }

    fun e(tag: String, e: Exception) {
        log(tag, Log.getStackTraceString(e), TYPE_E)
    }

    fun e(tag: String, tr: Throwable) {
        log(tag, Log.getStackTraceString(tr), TYPE_E)
    }

    fun e(tag: Any, msg: String) {
        log(tag.javaClass.simpleName, msg, TYPE_E)
    }

    fun e(tag: Any, e: Exception) {
        log(tag.javaClass.simpleName, Log.getStackTraceString(e), TYPE_E)
    }

    fun e(tag: Any, tr: Throwable) {
        log(tag.javaClass.simpleName, Log.getStackTraceString(tr), TYPE_E)
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        log(tag, msg + '\n'.toString() + Log.getStackTraceString(tr), TYPE_E)
    }

    // *******************Log with WARNING*******************/
    fun w(msg: String) {
        log(DEFAULT_TAG, msg, TYPE_W)
    }

    fun w(e: Exception) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_W)
    }

    fun w(tr: Throwable) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_W)
    }

    fun w(tag: String, msg: String) {
        log(tag, msg, TYPE_W)
    }

    fun w(tag: String, e: Exception) {
        log(tag, Log.getStackTraceString(e), TYPE_W)
    }

    fun w(tag: String, tr: Throwable) {
        log(tag, Log.getStackTraceString(tr), TYPE_W)
    }

    fun w(tag: Any, msg: String) {
        log(tag.javaClass.simpleName, msg, TYPE_W)
    }

    fun w(tag: Any, e: Exception) {
        log(tag.javaClass.simpleName, Log.getStackTraceString(e), TYPE_W)
    }

    fun w(tag: Any, tr: Throwable) {
        log(tag.javaClass.simpleName, Log.getStackTraceString(tr), TYPE_W)
    }

    fun w(tag: String, msg: String, tr: Throwable) {
        log(tag, msg + '\n'.toString() + Log.getStackTraceString(tr), TYPE_W)
    }

    // *******************Log with INFO*******************/
    fun i(msg: String) {
        log(DEFAULT_TAG, msg, TYPE_I)
    }

    fun i(tag: String, msg: String) {
        log(tag, msg, TYPE_I)
    }

    fun i(tag: Any, msg: String) {
        log(tag.javaClass.simpleName, msg, TYPE_I)
    }

    // *******************Log with DEBUG*******************/
    fun d(msg: String) {
        log(DEFAULT_TAG, msg, TYPE_D)
    }

    fun d(tag: String, msg: String) {
        log(tag, msg, TYPE_D)
    }

    fun d(tag: Any, msg: String) {
        log(tag.javaClass.simpleName, msg, TYPE_D)
    }

    // *******************Log with VERBOSE*******************/
    fun v(msg: String) {
        log(DEFAULT_TAG, msg, TYPE_V)
    }

    fun v(tag: String, msg: String) {
        log(tag, msg, TYPE_V)
    }

    fun v(tag: Any, msg: String) {
        log(tag.javaClass.simpleName, msg, TYPE_V)
    }

    private fun log(tag: String, msg: String, logType: Int) {
        var message = msg
        if (enable) {
            val stackTrace = Thread.currentThread().stackTrace[4]
            val fileInfo = "[" + stackTrace.fileName + "(" + stackTrace.lineNumber + ") " + stackTrace.methodName + "] "

            message = fileInfo + message

            var index = 0
            val maxLength = 4000
            val countOfSub = message.length / maxLength
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = message.substring(index, index + maxLength)
                    print(tag, sub, logType)
                    index += maxLength
                }
                print(tag, message.substring(index, message.length), logType)
            } else {
                print(tag, message, logType)
            }
        }
    }

    private fun print(tag: String, msg: String, logType: Int) {
        when (logType) {
            TYPE_V -> Log.v(tag, msg)
            TYPE_D -> Log.d(tag, msg)
            TYPE_I -> Log.i(tag, msg)
            TYPE_W -> Log.w(tag, msg)
            TYPE_E -> Log.e(tag, msg)
        }
    }
}