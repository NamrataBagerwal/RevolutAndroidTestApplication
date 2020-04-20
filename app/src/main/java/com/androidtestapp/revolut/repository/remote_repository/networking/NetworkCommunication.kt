package com.androidtestapp.revolut.repository.remote_repository.networking

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkCommunication {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    suspend fun getNetworkCommServiceResponse(url: String): Response {
        return httpClient.newCall(getRequest(url))
            .await()
    }

    private fun getRequest(url: String) = Request.Builder()
        .url(url)
        .build()

    private suspend fun Call.await(): Response {
        return suspendCancellableCoroutine { cancellableContinuation ->
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // No Implementation required
                }

                override fun onResponse(call: Call, response: Response) {
                    cancellableContinuation.resume(response) {
                        response.close()
                    }
                }
            })
            cancellableContinuation.invokeOnCancellation { cancel() }
        }
    }
}