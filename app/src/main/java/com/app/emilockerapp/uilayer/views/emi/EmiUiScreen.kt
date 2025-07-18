package com.app.emilockerapp.uilayer.views.emi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.emilockerapp.models.Device
import java.text.NumberFormat
import java.util.*

@Composable
fun EMIScreen() {
    val devices = listOf(
        Device("device-1", "Samsung Galaxy A50", 8500.toString(), "25th March", 5, 12,  8500),
        Device("device-2", "iPhone 12", 15000.toString(), "28th March", 2, 12,  15000),
        Device("device-3", "Xiaomi Redmi Note 10", 3200.toString(), "2nd April", 8, 10, 3200
        ),
    )

    var selectedDevice by remember { mutableStateOf(devices[0]) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            "EMI Management",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Track and manage your device payments",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Device", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(devices) { index, device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDevice = device },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedDevice.id == device.id) Color(0xFFE0F7FA) else Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(device.name, fontWeight = FontWeight.Medium)
                        Text(
                            "৳${formatAmount(device.dueAmount)} due on ${device.dueDate}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            device.status.uppercase(),
                            fontSize = 12.sp,
                            color = statusColor(device.status)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    selectedDevice.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("EMI Payment Details", color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "৳${formatAmount(selectedDevice.dueAmount)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text("Due on ${selectedDevice.dueDate}", color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = selectedDevice.emiPaid.toFloat() / selectedDevice.totalEmi,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${selectedDevice.emiPaid}/${selectedDevice.totalEmi} Paid",
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showPaymentDialog = true }) {
                    Text("Pay Now")
                }
            }
        }

        if (showPaymentDialog) {
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Confirm Payment") },
                text = {
                    Text(
                        "Proceed with payment for ${selectedDevice.name} - ৳${
                            formatAmount(
                                selectedDevice.dueAmount
                            )
                        }?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showPaymentDialog = false
                        // Add payment logic here
                    }) {
                        Text("Pay")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

fun formatAmount(amount: Int): String {
    return NumberFormat.getNumberInstance(Locale.US).format(amount)
}

fun statusColor(status: String): Color {
    return when (status) {
        "due" -> Color.Red
        "upcoming" -> Color.Blue
        "paid" -> Color.Green
        else -> Color.Gray
    }
}

@Preview
@Composable
fun PreviewEmiScreen() {
    EMIScreen()
}

