package com.network.state

internal interface INetworkStateManager {
    fun start(listener : IResponseEvent)
    fun stop(listener : IResponseEvent)
}