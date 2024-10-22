package com.network.state.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

object PingTask {

    fun ping(url: String, onComplete: (Boolean) -> Unit) {
        CoroutineScope(IO).launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 5000
                val responseCode = connection.responseCode
                onComplete(responseCode == HttpURLConnection.HTTP_OK)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}