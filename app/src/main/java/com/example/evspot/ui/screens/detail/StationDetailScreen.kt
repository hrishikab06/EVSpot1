package com.example.evspot.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.data.StationRepository
import com.example.evspot.data.api.Charger
import com.example.evspot.data.api.Station
import com.example.evspot.model.ChargingSlot
import com.example.evspot.ui.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(stationId: Int, onBack: () -> Unit, userViewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
    val stationRepository = remember { StationRepository() }
    val snackbarHostState = remember { SnackbarHostState() }

    var station by remember { mutableStateOf<Station?>(null) }
    var chargers by remember { mutableStateOf<List<Charger>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedChargerId by remember { mutableStateOf<Int?>(null) }
    
    // Booking confirmation state
    var showConfirmation by remember { mutableStateOf(false) }
    var bookingDetails by remember { mutableStateOf<com.example.evspot.data.api.BookingResponse?>(null) }

    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("GMT+05:30")
    }
    val displayFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"))
    val startCal = now.clone() as Calendar 
    val endCal = (startCal.clone() as Calendar).apply { add(Calendar.HOUR, 1) }

    LaunchedEffect(stationId) {
        isLoading = true
        stationRepository.getStationDetails(stationId).onSuccess {
            station = it
        }
        stationRepository.getChargers(stationId).onSuccess {
            chargers = it
        }
        isLoading = false
    }

    if (showConfirmation && bookingDetails != null) {
        BookingConfirmationDialog(
            details = bookingDetails!!,
            onDismiss = { showConfirmation = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(station?.name ?: "Station Details", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
                val isSelected = selectedChargerId != null
                Button(
                    onClick = { 
                        if (isSelected && station != null) {
                            val userId = userViewModel.userId
                            if (userId == null) {
                                scope.launch { snackbarHostState.showSnackbar("Please login first") }
                                return@Button
                            }

                            scope.launch {
                                // 1. Check Availability
                                val availResult = stationRepository.checkAvailability(
                                    selectedChargerId!!,
                                    isoFormatter.format(startCal.time),
                                    isoFormatter.format(endCal.time)
                                )

                                availResult.onSuccess { avail ->
                                    if (avail.available) {
                                        // 2. Create Booking
                                        val bookingResult = stationRepository.createBooking(
                                            userId = userId,
                                            chargerId = selectedChargerId!!,
                                            startTime = isoFormatter.format(startCal.time),
                                            endTime = isoFormatter.format(endCal.time),
                                            estimatedCost = 150
                                        )

                                        bookingResult.onSuccess { response ->
                                            bookingDetails = response
                                            showConfirmation = true
                                            // Refresh chargers
                                            stationRepository.getChargers(stationId).onSuccess { chargers = it }
                                        }.onFailure { error ->
                                            snackbarHostState.showSnackbar(error.message ?: "Booking failed")
                                        }
                                    } else {
                                        snackbarHostState.showSnackbar(avail.reason)
                                    }
                                }.onFailure { error ->
                                    snackbarHostState.showSnackbar(error.message ?: "Availability check failed")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(Icons.Default.EvStation, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Book ${displayFormatter.format(startCal.time)} - ${displayFormatter.format(endCal.time)}")
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else if (station != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Location: ${station!!.latitude}, ${station!!.longitude}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(station!!.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9).copy(alpha = 0.2f)) {
                                        Text(station!!.access_type ?: "Public", fontSize = 10.sp, color = Color(0xFF81C784), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("${station!!.address}, ${station!!.city}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (!chargers.any { it.is_available }) Color(0xFFFCE8E8).copy(alpha = 0.2f) else Color(0xFFE8F5E9).copy(alpha = 0.2f)
                            ) {
                                val availableCount = chargers.count { it.is_available }
                                Text(
                                    "$availableCount/${chargers.size} Available",
                                    fontSize = 11.sp,
                                    color = if (availableCount == 0) Color(0xFFE57373) else Color(0xFF81C784),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("4.5 (50 reviews)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("₹15/kWh", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Station Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailRow(Icons.Default.Business, "Operator", station!!.operator_name ?: "Unknown")
                        DetailRow(Icons.Default.Schedule, "Hours", station!!.opening_hours ?: "24/7")
                        DetailRow(Icons.Default.LocalTaxi, "Amenities", station!!.amenities ?: "None")
                    }
                }

                // Available Chargers Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Charging Points", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (chargers.isEmpty()) {
                            Text("No chargers found for this station.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            chargers.forEach { charger ->
                                val slot = ChargingSlot(
                                    id = charger.id,
                                    isAvailable = charger.is_available,
                                    type = "${charger.connector_type} - ${charger.power_kw}kW",
                                    statusText = if (charger.is_available) "Ready to charge" else "Occupied/Unavailable"
                                )
                                SlotRow(
                                    slot = slot,
                                    isSelected = selectedChargerId == charger.id,
                                    onClick = {
                                        if (charger.is_available) {
                                            selectedChargerId = if (selectedChargerId == charger.id) null else charger.id
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("This charger is currently busy")
                                            }
                                        }
                                    }
                                )
                                if (chargers.indexOf(charger) != chargers.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingConfirmationDialog(
    details: com.example.evspot.data.api.BookingResponse,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Booking Confirmed!", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Booking ID: #${details.booking_id}")
                Text("Status: ${details.status}")
                
                val isoIn = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
                val isoIn2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                val displayOut = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                
                val start = try { isoIn.parse(details.start_time) } catch (e: Exception) { isoIn2.parse(details.start_time) }
                val deadline = try { isoIn.parse(details.arrival_deadline) } catch (e: Exception) { isoIn2.parse(details.arrival_deadline) }
                
                if (start != null) {
                    Text("Start Time: ${displayOut.format(start)}")
                }
                if (deadline != null) {
                    Text("Arrival Deadline: ${displayOut.format(deadline)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
                
                Text("\nNote: Please arrive within 10 minutes of your start time or the booking will expire.", fontSize = 12.sp, color = Color.Gray)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun SlotRow(slot: ChargingSlot, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFE8F5E9).copy(alpha = 0.1f) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, Color(0xFF2E7D32)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = if (slot.isAvailable) Color(0xFFE8F5E9).copy(alpha = 0.2f) else Color(0xFFFCE8E8).copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (slot.isAvailable) Icons.Default.CheckCircle else Icons.Default.Block,
                            contentDescription = null,
                            tint = if (slot.isAvailable) Color(0xFF81C784) else Color(0xFFE57373),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Slot ${slot.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(slot.type, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    slot.statusText?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (slot.isAvailable) Color(0xFF81C784) else Color(0xFFE57373)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = if (slot.isAvailable) Color(0xFF81C784) else Color(0xFFE57373),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (slot.isAvailable) Color(0xFFE8F5E9).copy(alpha = 0.2f) else Color(0xFFFCE8E8).copy(alpha = 0.2f)
            ) {
                Text(
                    text = if (slot.isAvailable) "Available" else "Busy",
                    color = if (slot.isAvailable) Color(0xFF81C784) else Color(0xFFE57373),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
