package com.app.emilockerapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.emilockerapp.utils.scheduleWorker

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                context?.let {
                    scheduleWorker(it)
                }
            }
        }
    }
}