package com.network.state.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.network.state.IResponseEvent
import java.util.concurrent.TimeUnit

internal class Network {
    private var periodicWorkRequest: PeriodicWorkRequest? = null
    fun startService(context: Context, owner: LifecycleOwner, listener: IResponseEvent) {
        if (periodicWorkRequest == null) {
            val workManager: WorkManager = WorkManager.getInstance(context)
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            periodicWorkRequest = PeriodicWorkRequestBuilder<NetworkCheckerWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS
            ).setConstraints(constraints).build()
            workManager.enqueue(periodicWorkRequest!!)

            workInfoChecker(workManager, owner, listener)
        }
    }

    fun stopService(context: Context, owner: LifecycleOwner, listener: IResponseEvent) {
        if (periodicWorkRequest != null) {
            val workManager: WorkManager = WorkManager.getInstance(context)
            workManager.cancelWorkById(periodicWorkRequest!!.id)

            workInfoChecker(workManager, owner, listener)
        }
    }

    private fun workInfoChecker(
        workManager: WorkManager,
        owner: LifecycleOwner,
        listener: IResponseEvent
    ) {
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest!!.id).observe(owner) { workInfo ->
            if (workInfo == null) return@observe
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED -> {}

                WorkInfo.State.RUNNING -> {
                    listener.state(true)
                }

                WorkInfo.State.SUCCEEDED -> {}

                WorkInfo.State.FAILED -> {}

                WorkInfo.State.CANCELLED -> {}

                else -> {}
            }
        }
    }
}