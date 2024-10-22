package com.state.network

internal interface INetworkStateManager {
    fun start(listener : IResponseEvent)
    fun stop(listener : IResponseEvent)
}