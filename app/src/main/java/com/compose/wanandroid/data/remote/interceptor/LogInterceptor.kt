package com.compose.wanandroid.data.remote.interceptor

import android.net.Uri
import android.util.Log
import com.compose.wanandroid.logic.Logger
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.EOFException
import okio.GzipSource
import okio.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class LogInterceptor : Interceptor {

    companion object {
        private const val TAG = "Request"
        private val UTF8 = Charset.forName("UTF-8")
    }

    var level = Level.BASIC

    enum class Level {
        /** No logs. */
        NONE,

        /**
         * Logs request and response lines.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * ```
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * ```
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * ```
         */
        BODY
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val url = Uri.decode(request.url.toString())
        val method = request.method.uppercase()
        val protocol = chain.connection()?.protocol()?.toString() ?: ""
        var startMessage = "--> [$method] [$protocol] $url"
        if (request.headers.size > 0) {
            startMessage += "\n${request.headers}"
        }
        Logger.i(TAG, startMessage)

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        // 打印请求所有参数
        if (logHeaders) {
            val headers = request.headers

            val requestBody = request.body
            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        Logger.i(TAG, "Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        Logger.i(TAG, "Content-Length: ${requestBody.contentLength()}")
                    }
                }
            }

            for (i in 0 until headers.size) {
                Logger.i(TAG, headers.name(i) + ": " + headers.value(i))
            }

            if (!logBody || requestBody == null) {
                Logger.i(TAG, "--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                Logger.i(TAG, "--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                Logger.i(TAG, "--> END ${request.method} (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                Logger.i(TAG, "--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(UTF8) ?: UTF8

                Logger.i(TAG, "")
                if (buffer.isProbablyUtf8()) {
                    Logger.i(TAG, buffer.readString(charset))
                    Logger.i(TAG, "--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    Logger.i(TAG, "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Logger.e(TAG, "<-- [$method] FAILED: $url \n ${Log.getStackTraceString(e)}")
            throw e
        }

        // 打印请求响应参数
        if (logHeaders) {
            val headers = response.headers
            for (i in 0 until headers.size) {
                Logger.d(TAG, headers.name(i) + ": " + headers.value(i))
            }

            if (!logBody || !response.promisesBody()) {
                Logger.d(TAG, "<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                Logger.d(TAG, "<-- END HTTP (encoded body omitted)")
            } else {
                val responseBody = response.body ?: return response
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(UTF8) ?: UTF8

                if (!buffer.isProbablyUtf8()) {
                    Logger.d(TAG, "")
                    Logger.d(TAG, "<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    return response
                }

                if (responseBody.contentLength() != 0L) {
                    Logger.d(TAG, "")
                    Logger.d(TAG, buffer.clone().readString(charset))
                }

                if (gzippedLength != null) {
                    Logger.d(TAG, "<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    Logger.d(TAG, "<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val msg = "<-- [$method] [$protocol] ${response.code} ${response.message} ${Uri.decode(response.request.url.toString())} (${tookMs}ms)"
        if (response.isSuccessful) {
            Logger.w(TAG, msg)
        } else {
            Logger.e(TAG, msg)
        }

        return response
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }
}