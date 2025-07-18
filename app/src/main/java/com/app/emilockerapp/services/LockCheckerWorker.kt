package com.app.emilockerapp.services

import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.emilockerapp.uilayer.views.lockui.LockScreenActivity
import com.app.emilockerapp.utils.isEMIOverdue

class LockCheckerWorker(appcontext: Context, workerParams: WorkerParameters) :
    Worker(appcontext, workerParams) {
    override fun doWork(): Result {
        val context = applicationContext
        if (isEMIOverdue(context)) {
            val lockIntent = Intent(context, LockScreenActivity::class.java)
            lockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(lockIntent)
        }
        return Result.success()

    }
}