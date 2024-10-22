package com.state.network

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.state.network.service.ConnectivityReceiver

class
NetworkStateManager(private val context: Context, private val owner: LifecycleOwner) :
    INetworkStateManager {

    companion object {
        const val URL_ADDRESS = "http://10.0.2.2:5096/Note/"
    }

    override fun start(listener: IResponseEvent) {
        connectivityReceiverState(context, true, listener)
    }

    override fun stop(listener: IResponseEvent) {
        connectivityReceiverState(context, false, listener)
    }

    private fun connectivityReceiverState(
        context: Context,
        state: Boolean,
        listener: IResponseEvent
    ) {
        if (state) {
            ConnectivityReceiver(listener).register(context)
        } else {
            ConnectivityReceiver(listener).unregister()
        }
    }
}