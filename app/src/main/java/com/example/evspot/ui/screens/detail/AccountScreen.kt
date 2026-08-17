package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.ThemeMode
import com.example.evspot.ui.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onBack: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileCard()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    AccountRow(Icons.Default.AccountBalanceWallet, "Wallet", "Manage your balance and payments")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.SwitchAccount, "Switch Account", "Switch between your accounts")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.PersonAdd, "Add Account", "Add a new account")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.Edit, "Edit Profile", "Update your personal information")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.HelpOutline, "Help Centre", "Get help and support")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.Shield, "Security & Privacy", "Manage your security and privacy")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(Icons.Default.Settings, "Settings", "Manage app preferences")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    AccountRow(
                        icon = Icons.Default.Palette,
                        title = "App Theme",
                        subtitle = "Light, Dark or System Default",
                        onClick = { showThemeDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    AccountRow(
                        icon = Icons.Default.Logout,
                        title = "Account Logout",
                        subtitle = "Logout from your account",
                        isDestructive = true
                    )
                }
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentMode = themeViewModel.themeMode.value,
            onDismiss = { showThemeDialog = false },
            onSelect = { mode ->
                themeViewModel.setThemeMode(mode)
                showThemeDialog = false
            }
        )
    }
}

@Composable
fun ThemeSelectionDialog(
    currentMode: ThemeMode,
    onDismiss: () -> Unit,
    onSelect: (ThemeMode) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select App Theme") },
        text = {
            Column {
                ThemeOptionRow("Light", currentMode == ThemeMode.LIGHT) { onSelect(ThemeMode.LIGHT) }
                ThemeOptionRow("Dark", currentMode == ThemeMode.DARK) { onSelect(ThemeMode.DARK) }
                ThemeOptionRow("System Default", currentMode == ThemeMode.DEFAULT) { onSelect(ThemeMode.DEFAULT) }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ThemeOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = Color(0xFFE8F5E9), modifier = Modifier.size(56.dp)) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Aryan Jaiswal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("aryan.jaiswal@gmail.com", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("EV Driver", fontSize = 11.sp, color = Color(0xFF2E7D32))
                    }
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun AccountRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isDestructive) Color(0xFFFCE8E8) else Color(0xFFE8F5E9),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isDestructive) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isDestructive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
            )
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = if (isDestructive) Color(0xFFD32F2F) else Color.Gray
        )
    }
}
