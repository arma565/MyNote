package com.android_learn.mynote.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager

class Utility {

    fun triggerRebirth(context: Activity) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }



}