package com.network.state

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.network.state.service.ConnectivityReceiver
import com.network.state.service.Network
import com.network.state.service.PingTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class NetworkStateManager(private val context: Context,private  val owner: LifecycleOwner) : INetworkStateManager {
    override fun start(listener: IResponseEvent) {
        try {
            PingTask.ping("https://androidsupport.ir/pack/"){isServerRespond->
                if (!isServerRespond){
                    CoroutineScope(Main).launch {
                        listener.serverState(false)
                        return@launch
                    }
                }
            }
            connectivityReceiverState(context,true,listener)
            Network().startService(context,owner,listener)
        }catch (e : Exception){
            throw Error("Unknown system error!",e)
        }
    }

    override fun stop(listener: IResponseEvent) {
        try {
            connectivityReceiverState(context,false,listener)
            Network().stopService(context,owner,listener)
        }catch (e : Exception){
            throw Error("Unknown system error!",e)
        }
    }

    private fun connectivityReceiverState(context : Context,state : Boolean,listener: IResponseEvent){
        if (state){
            ConnectivityReceiver(listener).register(context)
            return
        }
        ConnectivityReceiver(listener).unregister()
    }
}