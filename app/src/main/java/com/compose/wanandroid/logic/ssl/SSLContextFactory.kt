package com.compose.wanandroid.logic.ssl

import android.util.Base64
import java.io.*
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLContextFactory {

    /**
     * Creates an SSLContext with trust all certificates
     *
     * @return An initialized SSLContext
     */
    fun makeContext(): SSLContext {
        val context: SSLContext = try {
            SSLContext.getInstance("TLS")
        } catch (e: Exception) {
            SSLContext.getInstance("LLS")
        }
        context.init(null, arrayOf<X509TrustManager>(SimpleX509TrustManager()), SecureRandom())
        return context
    }

    /**
     * Creates an SSLContext with the KeyStore certificates
     *
     * @param keyStore a keyStore
     * @return An initialized SSLContext
     */
    fun makeContext(keyStore: KeyStore): SSLContext {
        val context: SSLContext = try {
            SSLContext.getInstance("TLS")
        } catch (e: Exception) {
            SSLContext.getInstance("LLS")
        }
        context.init(null, arrayOf<X509TrustManager>(GenericTrustManager(keyStore)), SecureRandom())
        return context
    }

    /**
     * Creates an SSLContext with the client and server certificates
     *
     * @param clientCertFile     A File containing the client certificate
     * @param clientCertPassword Password for the client certificate
     * @param caCertString       A String containing the server certificate
     * @return An initialized SSLContext
     */
    fun makeContext(clientCertFile: File, clientCertPassword: String, caCertString: String): SSLContext {
        val keyStore = loadPKCS12KeyStore(clientCertFile, clientCertPassword)
        val kmf = KeyManagerFactory.getInstance("X509")
        kmf.init(keyStore, clientCertPassword.toCharArray())
        val keyManagers = kmf.keyManagers

        val trustStore = loadPEMTrustStore(caCertString)
        val trustManagers = arrayOf<TrustManager>(GenericTrustManager(trustStore))

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagers, trustManagers, null)
        return sslContext
    }

    /**
     * Creates an SSLContext with the server certificates
     *
     * @param caCertString A String containing the server certificate
     * @return An initialized SSLContext
     * @throws Exception
     */
    fun makeContext(caCertString: String): SSLContext {
        val trustStore = loadPEMTrustStore(caCertString)
        val trustManagers = arrayOf<TrustManager>(GenericTrustManager(trustStore))
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagers, null)
        return sslContext
    }

    /**
     * Produces a KeyStore from a PKCS12 (.p12) certificate file, typically the client certificate
     *
     * @param certificateFile    A file containing the client certificate
     * @param clientCertPassword Password for the certificate
     * @return A KeyStore containing the certificate from the certificateFile
     * @throws Exception
     */
    private fun loadPKCS12KeyStore(certificateFile: File, clientCertPassword: String): KeyStore {
        val keyStore: KeyStore
        var fis: FileInputStream? = null
        try {
            keyStore = KeyStore.getInstance("PKCS12")
            fis = FileInputStream(certificateFile)
            keyStore.load(fis, clientCertPassword.toCharArray())
        } finally {
            try {
                fis?.close()
            } catch (ex: IOException) {
                // ignore
            }
        }
        return keyStore
    }

    /**
     * Produces a KeyStore from a String containing a PEM certificate (typically, the server's CA certificate)
     *
     * @param certificateString A String containing the PEM-encoded certificate
     * @return a KeyStore (to be used as a trust store) that contains the certificate
     * @throws Exception
     */
    private fun loadPEMTrustStore(certificateString: String): KeyStore {
        val der = loadPemCertificate(ByteArrayInputStream(certificateString.toByteArray()))
        val derInputStream = ByteArrayInputStream(der)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val cert = certificateFactory.generateCertificate(derInputStream) as X509Certificate
        val alias = cert.subjectX500Principal.name

        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
        trustStore.load(null)
        trustStore.setCertificateEntry(alias, cert)
        return trustStore
    }

    /**
     * Reads and decodes a base-64 encoded DER certificate (a .pem certificate), typically the server's CA cert.
     *
     * @param certificateStream an InputStream from which to read the cert
     * @return a byte[] containing the decoded certificate
     * @throws IOException
     */
    private fun loadPemCertificate(certificateStream: InputStream): ByteArray {
        val der: ByteArray
        val br: BufferedReader? = null

        br.use {
            val buf = StringBuilder()
            certificateStream.reader().buffered().readLines().forEach { line ->
                if (!line.startsWith("--")) {
                    buf.append(line)
                }
            }
            val pem = buf.toString()
            der = Base64.decode(pem, Base64.DEFAULT)
        }
        return der
    }
}