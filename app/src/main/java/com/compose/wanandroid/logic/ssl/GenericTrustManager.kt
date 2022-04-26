package com.compose.wanandroid.logic.ssl

import android.annotation.SuppressLint
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
class GenericTrustManager(vararg keyStores: KeyStore) : X509TrustManager {

    private var trustManagers = mutableListOf<X509TrustManager>()

    init {
        val factories = mutableListOf<TrustManagerFactory>()
        try {
            // The default TrustManager with default keystore
            val original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            original.init(null as KeyStore?)
            factories.add(original)

            for (keyStore in keyStores) {
                val certs = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                certs.init(keyStore)
                factories.add(certs)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        trustManagers.addAll(factories.asSequence().flatMap { it.trustManagers.asSequence() }.filterIsInstance<X509TrustManager>())

        if (trustManagers.isEmpty()) {
            throw RuntimeException("Couldn't find any X509TrustManagers")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        val list = mutableListOf<X509Certificate>()
        for (tm in trustManagers) {
            list.addAll(tm.acceptedIssuers)
        }
        return list.toTypedArray()
    }

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        // Delegate to the default trust manager.
        val defaultX509TrustManager = trustManagers[0]
        defaultX509TrustManager.checkClientTrusted(chain, authType)
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        // Loop over the TrustManagers until we find one that accepts our server
        for (tm in trustManagers) {
            try {
                tm.checkServerTrusted(chain, authType)
                return
            } catch (e: CertificateException) {
                // ignore
            }
        }
        throw CertificateException()
    }
}