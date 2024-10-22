package com.state.network.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.state.network.IResponseEvent
import com.state.network.NetworkStateManager.Companion.URL_ADDRESS
import java.net.HttpURLConnection
import java.net.URL

class ConnectivityReceiver(private val listener: IResponseEvent) {

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun register(context: Context) {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                ping(URL_ADDRESS) { isServerRespond ->
                    if (!isServerRespond) {
                        listener.networkState(false)
                        return@ping
                    }
                    listener.networkState(true)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.networkState(false)
            }
        }

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun unregister() {
        connectivityManager?.unregisterNetworkCallback(networkCallback!!)
    }

    private fun ping(url: String, onComplete: (Boolean) -> Unit) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        val responseCode = connection.responseCode
        onComplete(responseCode == HttpURLConnection.HTTP_OK)
    }
}