package com.app.emilockerapp.uilayer.views.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.emilockerapp.R
import com.app.emilockerapp.models.Device
import com.app.emilockerapp.models.NotificationItem
import com.app.emilockerapp.uilayer.views.emi.EmiScreen
import com.app.emilockerapp.uilayer.views.security.SecurityScreen
import com.app.emilockerapp.uilayer.views.auth.LoginScreen
import com.app.emilockerapp.utils.ComposeBaseExtensions.routeWithArgs
import java.text.SimpleDateFormat
import java.util.*

val sampleDevices = listOf(
    Device("1", "Samsung Galaxy A50", "Galaxy A50", "active", 5, 12, 8500),
    Device("2", "iPhone 12", "iPhone 12", "locked", 2, 12, 15000),
    Device("3", "Xiaomi Redmi Note 10", "Redmi Note 10", "active", 8, 10, 3200),
)

val sampleNotifications = listOf(
    NotificationItem(1, "EMI Due Tomorrow", "Samsung Galaxy A50 - ৳8,500", "2 min ago"),
    NotificationItem(2, "Device Locked", "iPhone 12 locked remotely", "1 hour ago"),
    NotificationItem(3, "Payment Received", "EMI payment successful", "3 hours ago")
)

@Composable
fun CustomBottomNavigation(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    navController: NavHostController,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomBottomNavItem(
            icon = R.drawable.locker_home_ic,
            contentDescription = "Home",
            isSelected = selectedIndex == 0,
            onClick = { onItemClick(0) }
        )
        CustomBottomNavItem(
            icon = R.drawable.locler_emi_business,
            contentDescription = "Emi",
            isSelected = selectedIndex == 1,
            onClick = {
                onItemClick(1)
                navController.navigate(routeWithArgs(EmiScreen.Routes.emiScreen))
            }
        )
        CustomBottomNavItem(
            icon = R.drawable.locker_settings_ic,
            contentDescription = "Settings",
            isSelected = selectedIndex == 2,
            onClick = {
                onItemClick(2)
                navController.navigate(routeWithArgs(LoginScreen.Routes.emi_Login_Screen))
            }
        )

        CustomBottomNavItem(
            icon = R.drawable.security_ic,
            contentDescription = "Settings",
            isSelected = selectedIndex == 2,
            onClick = {
                onItemClick(3)
                navController.navigate(routeWithArgs(SecurityScreen.Routes.emiSecurityScreen))
            }
        )
    }
}

@Composable
fun CustomBottomNavItem(
    icon: Int,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .wrapContentSize()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val devices by remember { mutableStateOf(sampleDevices) }
    val notifications = remember { mutableStateListOf(*sampleNotifications.toTypedArray()) }
    val isOnline by remember { mutableStateOf(true) }
    val currentTime by rememberUpdatedState(newValue = Date())

    val totalDue = devices.sumOf { it.dueAmount }
    val totalPaid = devices.sumOf { it.emiPaid }
    val totalEmis = devices.sumOf { it.totalEmi }
    var isEMILocked by remember { mutableStateOf(false) }
    val paidPercent = if (totalEmis > 0) (totalPaid * 100 / totalEmis) else 0
    val activeCount = devices.count { it.status == "active" }
    val lockedCount = devices.count { it.status == "locked" }

//    LaunchedEffect(Unit) {
//        if (shouldSkipLock(context)) {
//            clearSkipLock(context) // Skip lock once and reset
//        } else {
//            if (paidPercent < 60) {
//                setEMILocked(context, true)
//                val lockIntent = Intent(context, LockScreenActivity::class.java)
//                lockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(lockIntent)
//            } else {
//                setEMILocked(context, false)
//            }
//        }
//    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {


        item {
            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(SimpleDateFormat("EEE, MMM dd", Locale.US).format(currentTime), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            if (!isOnline) {
                Text(
                    text = "You're offline. Changes will sync when back online.",
                    color = Color.Yellow,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        if (notifications.isNotEmpty()) {
            item {
                Text("Recent Notifications", fontWeight = FontWeight.SemiBold)
                notifications.take(3).forEach { notif ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .clickable { notifications.remove(notif) },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(notif.title, fontWeight = FontWeight.Medium)
                            Text(notif.message, fontSize = 12.sp, color = Color.Gray)
                            Text(notif.time, fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DashboardCard(
                    "Total Devices",
                    "${devices.size}",
                    "$activeCount active, $lockedCount locked"
                )
                DashboardCard("Total Due", "৳${totalDue / 1000}K", "$paidPercent% completed")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(devices) { device ->
            DeviceCard(device = device, onAction = { action ->
                // Handle lock/unlock/track/payment actions here
            })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, subtitle: String) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(title, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Text(subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
        }
    }
}

@Composable
fun DeviceCard(device: Device, onAction: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(device.name, fontWeight = FontWeight.Bold)
                    Text(device.model, fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = device.status.uppercase(),
                    color = if (device.status == "locked") Color.Red else Color.Green,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = device.emiPaid / device.totalEmi.toFloat())
            Spacer(modifier = Modifier.height(4.dp))
            Text("Due: ৳${device.dueAmount}", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onAction(if (device.status == "locked") "unlock" else "lock") }) {
                    Text(if (device.status == "locked") "Unlock" else "Lock")
                }
                Button(onClick = { onAction("track") }) {
                    Text("Track")
                }
                Button(onClick = { onAction("payment") }) {
                    Text("Pay EMI")
                }
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}



