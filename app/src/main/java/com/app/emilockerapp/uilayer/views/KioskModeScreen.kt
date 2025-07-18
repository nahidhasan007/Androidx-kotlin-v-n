package com.app.emilockerapp.uilayer.views

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.emilockerapp.services.DeviceAdminReceiver
import com.app.emilockerapp.utils.ComposeBaseExtensions.findActivity

@Composable
fun KioskControllerScreen(onClick: () -> Unit, enableKiosk: () -> Unit) {
    val context = LocalContext.current
    val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val component = ComponentName(context, DeviceAdminReceiver::class.java)

    var isKioskMode by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            enableKiosk.invoke()
//            if (dpm.isDeviceOwnerApp(context.packageName)) {
//
//                if (!isKioskMode) {
//                    dpm.setLockTaskPackages(component, arrayOf(context.packageName))
////                    activity?.startLockTask()
//                    context.findActivity()?.startLockTask()
//                    isKioskMode = true
//                } else {
////                    activity?.stopLockTask()
//                    context.findActivity()?.stopLockTask()
//                    isKioskMode = false
//                }
//            } else {
//                Toast.makeText(context, "App is not Device Owner", Toast.LENGTH_SHORT).show()
//            }
        }) {
            Text(if (isKioskMode) "Exit Kiosk Mode" else "Enter Kiosk Mode")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            onClick.invoke()
//            if (dpm.isAdminActive(component)) {
//                dpm.removeActiveAdmin(component)
//                Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show()
//            } else {
//                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component)
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please enable admin to control kiosk mode.")
//                context.startActivity(intent)
//            }
        }) {
            Text("Disable Device Admin")
        }
    }
}
