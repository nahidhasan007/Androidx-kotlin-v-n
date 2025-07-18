package com.app.emilockerapp.uilayer.views.lockui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LockScreenUI(onAppClick: () -> Unit, onEmergencyClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Text("Phone Locked", color = Color.White, fontSize = 26.sp)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onAppClick) { Text("Open EMI Locker App") }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onEmergencyClick) { Text("Emergency Call") }
        }
    }
}
