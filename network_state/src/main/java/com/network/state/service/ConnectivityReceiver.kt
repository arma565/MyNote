package com.network.state.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.network.state.IResponseEvent

class ConnectivityReceiver(private val listener: IResponseEvent) {

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun register(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.state(true)
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                listener.state(false)
            }
        }

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun unregister() {
        connectivityManager?.unregisterNetworkCallback(networkCallback!!)
    }
}