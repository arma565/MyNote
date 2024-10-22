package com.network.state.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.Worker
import androidx.work.WorkerParameters

internal class NetworkCheckerWorker(private val context: Context,params : WorkerParameters) : Worker(context,params)  {
    override fun doWork(): Result {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            if (isConnected) {
                Result.success()
            }else{
                Result.failure()
            }
        }catch (e : Exception){
            Result.failure()
        }
    }
}