package com.network.state
  interface IResponseEvent {
    fun state(state : Boolean)
    fun serverState(state: Boolean)
}