package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.MainActivity

data class WalletTransaction(
    val title: String,
    val subtitle: String,
    val amount: Int,
    val isCredit: Boolean
)

val sampleTransactions = listOf(
    WalletTransaction("Wallet Top-up", "17 Aug 2026, 4:50 PM", 500, isCredit = true),
    WalletTransaction("GreenCharge Station", "15 Aug 2026, 8:30 PM", 312, isCredit = false),
    WalletTransaction("Marine Drive Charging", "12 Aug 2026, 6:15 PM", 454, isCredit = false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallet") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Available Balance", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("₹1,240", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                (context as? MainActivity)?.startWalletTopUp(500)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B5E20), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Money", color = Color(0xFF1B5E20))
                        }
                    }
                }
            }

            item {
                Text("Recent Transactions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(sampleTransactions) { txn ->
                TransactionRow(txn)
            }
        }
    }
}

@Composable
fun TransactionRow(txn: WalletTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (txn.isCredit) Color(0xFFE8F5E9) else Color(0xFFFCE8E8),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    if (txn.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (txn.isCredit) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(txn.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(txn.subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                text = "${if (txn.isCredit) "+" else "-"}₹${txn.amount}",
                fontWeight = FontWeight.Bold,
                color = if (txn.isCredit) Color(0xFF2E7D32) else Color(0xFFD32F2F)
            )
        }
    }
}