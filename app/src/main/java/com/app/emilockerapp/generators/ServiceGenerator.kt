package com.app.emilockerapp.generators

import android.util.Log
import com.app.emilockerapp.models.basemodels.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator {

    private val lockerBaseUrl = "https://dev.locker.com/v1/".toHttpUrlOrNull()

    // Authorization token placeholder
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Content-Type", "application/json")
            // Add Authorization header if token is available
            authToken?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()

    // Create Moshi instance with KotlinJsonAdapterFactory
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Create MoshiConverterFactory
    private val moshiConverterFactory = MoshiConverterFactory.create(moshi)

    private val retrofitBuilder = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(moshiConverterFactory)

    private val retrofitBuilderWithNullValue = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(moshiConverterFactory.withNullSerialization())

    private val retrofit = retrofitBuilder
        .baseUrl(lockerBaseUrl!!)
        .build()

    private val retrofitWithNullValue = retrofitBuilderWithNullValue
        .baseUrl(lockerBaseUrl!!)
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun <S> createServiceWithNullValueEnabled(serviceClass: Class<S>): S {
        return retrofitWithNullValue.create(serviceClass)
    }

    private fun createRetrofit(baseUrl: String) = retrofitBuilder.baseUrl(baseUrl).build()

    fun <S> generate(serviceClass: Class<S>, serviceBaseURL: String): S {
        return createRetrofit(serviceBaseURL).create(serviceClass)
    }

    fun connectWebSocket(url: String, listener: WebSocketListener) {
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $authToken") // Use the token if available
            .build()

        val webSocket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()  // Optional: Shutdown the client when not needed
    }

    fun sendMessage(webSocket: WebSocket, message: String) {
        webSocket.send(message)  // Send a message over the WebSocket
    }

    fun closeWebSocket(webSocket: WebSocket) {
        webSocket.close(1000, "Closing connection")  // Close WebSocket with a code and reason
    }
}

class MyWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "Connected to WebSocket")
        webSocket.send("Hello Server!") // Send a message after connection
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Received text: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WebSocket", "Received binary: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Closing: $code / $reason")
        webSocket.close(1000, "Closing WebSocket")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Closed: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Error: ${t.message}")
    }
}