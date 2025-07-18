package com.app.emilockerapp.uilayer.views.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

@Composable
fun LoginUiScreen() {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        InputTextField(label = "UserName", value = username, onValueChange = { uName ->
            username = uName
        })
        InputTextField(label = "Password", value = password, onValueChange = { pass ->
            password = pass
        })
        ActionButtonText(buttonText = "Login", buttonColor = Color.LightGray) {

        }

    }


}


@Composable
fun ActionButtonText(
    buttonText: String,
    buttonColor: Color = Color.Blue,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // outer margin
            .height(48.dp)
            .background(buttonColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500,
                color = Color.White
            )
        )
    }
}


@Composable
fun InputTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}


@Composable
fun AppManagementSection(isUpdating: Boolean, onStart: () -> Unit, onFinish: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("App Management", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                onStart()
                /*LaunchedEffect(true) {
                    delay(2000)
                    onFinish()
                }*/
            }, enabled = !isUpdating) {
                if (isUpdating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (isUpdating) "Checking..." else "Check for Updates")
            }
        }
    }
}

@Composable
fun PreferencesSection(
    isDark: Boolean,
    notifications: Boolean,
    biometric: Boolean,
    sound: Boolean,
    sync: Boolean,
    onToggle: (String, Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Preferences", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            ToggleRow("Notifications", notifications) { onToggle("notifications", it) }
            ToggleRow("Dark Mode", isDark) { onToggle("darkMode", it) }
            ToggleRow("Biometric Auth", biometric) { onToggle("biometricAuth", it) }
            ToggleRow("Sound", sound) { onToggle("soundEnabled", it) }
            ToggleRow("Auto Sync", sync) { onToggle("autoSync", it) }
        }
    }
}

@Composable
fun ToggleRow(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = state, onCheckedChange = onToggle)
    }
}

@Composable
fun AdvancedSection(onStorageClick: () -> Unit, onExportClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Advanced", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("Storage Management",
                Modifier
                    .clickable { onStorageClick() }
                    .padding(8.dp))
            Text("Export Data",
                Modifier
                    .clickable { onExportClick() }
                    .padding(8.dp))
        }
    }
}

@Composable
fun SignOutSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .clickable { /* Sign out */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red)
            Spacer(Modifier.width(12.dp))
            Text("Sign Out", color = Color.Red)
        }
    }
}

@Composable
fun AppInfoFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("EMI Locker PWA v1.0.0", fontSize = 12.sp, color = Color.Gray)
        Text("Made with ❤️ in 🇧🇩 Bangladesh", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun StorageDialog(onDismiss: () -> Unit, onClear: () -> Unit, onExport: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onExport()
                onDismiss()
            }) {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Storage Management") },
        text = {
            Text("You have used 123.45 KB of storage. Clear all app data or export your data.")
        }
    )
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    InputTextField("Username", value = "", onValueChange = {})
}
