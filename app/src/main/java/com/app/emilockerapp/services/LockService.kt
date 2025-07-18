package com.app.emilockerapp.services

import android.app.*
import android.content.*
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.admin.DevicePolicyManager
import com.app.emilockerapp.R
import kotlinx.coroutines.*

class LockService : Service() {

    private val channelId = "lock_channel"
    private val notificationId = 101
    private val deviceId = "YOUR_DEVICE_ID" // Set or generate your device identifier
    private val componentName by lazy {
        ComponentName(this, DeviceAdminReceiver::class.java)
    }

    private val dpm by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, buildNotification(getString(R.string.emi_app_running)))
//        startLockStatusMonitor()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.emi_lock_text))
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Device Lock Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors and controls device lock status"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*private fun startLockStatusMonitor() {
        serviceScope.launch {
            val api = createApi()
            while (isActive) {
                try {
                    val response = api.getDeviceLockStatus(deviceId)
                    if (response.locked) {
                        lockDevice()
                    } else {
                        unlockDevice()
                    }
                } catch (e: Exception) {
                    Log.e("MainService", "Error checking lock status", e)
                }
                delay(10_000) // Poll every 10 seconds
            }
        }
    }

    private fun lockDevice() {
        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setLockTaskPackages(componentName, arrayOf(packageName))
            if (!isInLockTaskMode()) {
                startLockTask()
            }
        }
    }

    private fun unlockDevice() {
        if (isInLockTaskMode()) {
            stopLockTask()
        }
    }
     */

    private fun isInLockTaskMode(): Boolean {
        return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
    }
}
