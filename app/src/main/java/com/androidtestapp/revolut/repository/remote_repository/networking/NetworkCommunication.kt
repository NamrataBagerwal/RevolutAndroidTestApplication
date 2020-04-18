package com.androidtestapp.revolut.repository.remote_repository.networking

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object NetworkCommunication {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val spec =  ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
        .build()
    private val httpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
//            .connectionSpecs(Collections.singletonList(spec))
//            .retryOnConnectionFailure(true)
//            .protocols(listOf(Protocol.HTTP_1_1))
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(15, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    suspend fun getNetworkCommServiceResponse(url: String): Response{
        return httpClient.newCall(getRequest(url))
            .await()
    }

    private fun getRequest(url: String) = Request.Builder()
        .url(url)
        .build()

    private suspend fun Call.await(): Response{
        return suspendCancellableCoroutine { cancellableContinuation ->
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    cancellableContinuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    cancellableContinuation.resume(response){
                        response.close()
                    }
                }
            })
            cancellableContinuation.invokeOnCancellation { cancel()  }
        }
    }
}