package com.androidtestapp.revolut.repository.remotedatastore.networking

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resumeWithException

object NetworkCommunication {

    private val httpClient by lazy { OkHttpClient.Builder().build() }

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
                    cancellableContinuation.resumeWithException(e)
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