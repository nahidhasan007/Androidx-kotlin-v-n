package com.app.emilockerapp.uilayer.views.security

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun SecurityScreenView() {
    var selectedDevice by remember { mutableStateOf(devices[0]) }
    var securitySettings by remember {
        mutableStateOf(
            mutableMapOf(
                "autoLock" to true,
                "biometric" to false,
                "remoteWipe" to false,
                "locationAlert" to true,
                "appLock" to true,
                "screenCapture" to false
            )
        )
    }
    var appLocks by remember {
        mutableStateOf(
            mutableMapOf(
                "whatsapp" to true,
                "facebook" to true,
                "youtube" to false,
                "instagram" to false,
                "gallery" to true,
                "camera" to false,
                "settings" to true,
                "banking" to true
            )
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Device Security", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Control device and app access remotely", color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            Text("Select Device", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
        }

        items(devices) { device ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    .clickable { selectedDevice = device }
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(10.dp).background(
                            if (device.status == "locked") Color.Red else Color.Green,
                            shape = CircleShape
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(device.name, fontWeight = FontWeight.Medium)
                        Text("${device.apps.locked}/${device.apps.total} apps locked", fontSize = 12.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(device.security, fontSize = 12.sp, color = Color.Blue)
                        Text(device.lastAction, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        selectedDevice = selectedDevice.copy(
                            status = if (selectedDevice.status == "locked") "active" else "locked"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDevice.status == "locked") Color(0xFFD32F2F) else Color(0xFF388E3C),
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        imageVector = if (selectedDevice.status == "locked") Icons.Default.Lock else Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Device ${selectedDevice.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "${selectedDevice.name} is currently ${if (selectedDevice.status == "locked") "secured" else "accessible"}",
                color = Color.Gray,
            )
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text("Security Settings", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            securitySettings.forEach { (key, value) ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(key.replaceFirstChar { it.uppercase() }, fontSize = 14.sp)
                    Switch(checked = value, onCheckedChange = {
                        securitySettings = securitySettings.toMutableMap().apply { put(key, !value) }
                    })
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text("App Lock Control", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
        }

        items(appLocks.keys.toList()) { appId ->
            val isLocked = appLocks[appId] == true
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(appId.replaceFirstChar { it.uppercase() }, fontSize = 14.sp)
                Switch(checked = isLocked, onCheckedChange = {
                    appLocks = appLocks.toMutableMap().apply { put(appId, !isLocked) }
                })
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text("Security Activity", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
        }

        items(securityLogs) { log ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(log.action, fontWeight = FontWeight.Medium)
                        Text(log.time, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

// Dummy data model and data

data class Device(val id: String, val name: String, val status: String, val lastAction: String, val security: String, val apps: Apps)
data class Apps(val locked: Int, val total: Int)
data class SecurityLog(val id: Int, val action: String, val time: String)

val devices = listOf(
    Device("device-1", "Samsung Galaxy A50", "locked", "2 hours ago", "high", Apps(3, 8)),
    Device("device-2", "iPhone 12", "active", "5 minutes ago", "medium", Apps(1, 12)),
    Device("device-3", "Xiaomi Redmi Note 10", "active", "1 hour ago", "high", Apps(4, 6))
)

val securityLogs = listOf(
    SecurityLog(1, "Device locked remotely", "2 hours ago"),
    SecurityLog(2, "Failed unlock attempt", "3 hours ago"),
    SecurityLog(3, "WhatsApp locked", "5 hours ago"),
    SecurityLog(4, "Location alert triggered", "1 day ago"),
    SecurityLog(5, "Biometric authentication enabled", "2 days ago")
)
