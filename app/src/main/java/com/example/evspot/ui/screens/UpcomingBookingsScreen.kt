package com.example.evspot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evspot.ui.theme.EVSpotTheme
import com.example.evspot.ui.theme.VoltGreen
import com.example.evspot.ui.theme.PaleGreen
import com.example.evspot.ui.theme.ErrorRed

data class Booking(
    val id: String,
    val stationName: String,
    val location: String,
    val connectorType: String,
    val connectorPower: String,
    val date: String,
    val weekday: String,
    val timeRange: String,
    val durationText: String,
    val price: String,
    val status: String,
    val isDC: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingBookingsScreen(
    onBack: () -> Unit = {}
) {
    val bookings: List<Booking> = sampleBookings
    
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Booking status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box {
                                IconButton(onClick = { /* TODO */ }) {
                                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                                }
                                Surface(
                                    color = ErrorRed,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 8.dp, end = 8.dp)
                                        .size(16.dp)
                                ) {
                                    Text(
                                        text = "3",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = CircleShape,
                                color = Color.LightGray,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                
                BookingTabs()
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader()
            }
            
            items(bookings) { booking ->
                BookingCard(booking)
            }
            
            item {
                InfoBanner()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun BookingTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TabItem("Upcoming (3)", selected = true)
        TabItem("Completed", selected = false)
        TabItem("Cancelled", selected = false)
    }
}

@Composable
fun TabItem(text: String, selected: Boolean) {
    Surface(
        color = if (selected) Color(0xFF1B5E20) else Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        onClick = { /* TODO */ }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SectionHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Your Bookings",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Surface(
            color = PaleGreen,
            shape = RoundedCornerShape(8.dp),
            onClick = { /* TODO */ }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = VoltGreen
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Filter", color = VoltGreen, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Upcoming",
                    color = VoltGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(PaleGreen, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Text(
                    "Booking ID: #${booking.id}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.Top) {
                Box {
                    Surface(
                        color = PaleGreen,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = null,
                            tint = VoltGreen,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Surface(
                        color = Color(0xFF1B5E20),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                    ) {
                        Text(
                            if (booking.isDC) "DC" else "AC",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.stationName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            booking.location,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        booking.price,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Est. Cost",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                BookingDetailItem(
                    Modifier.weight(1f),
                    Icons.Outlined.CalendarToday,
                    "Date",
                    booking.date,
                    booking.weekday
                )
                BookingDetailItem(
                    Modifier.weight(1.3f),
                    Icons.Outlined.Schedule,
                    "Time",
                    booking.timeRange,
                    booking.durationText
                )
                BookingDetailItem(
                    Modifier.weight(1f),
                    Icons.Outlined.Power,
                    "Connector",
                    booking.connectorType,
                    booking.connectorPower
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                color = PaleGreen,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = VoltGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Confirmed",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = VoltGreen
                            )
                            Text(
                                "Your slot is reserved",
                                fontSize = 12.sp,
                                color = VoltGreen.copy(alpha = 0.8f)
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VoltGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VoltGreen),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("View Details", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailItem(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = VoltGreen
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 10.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(subValue, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
fun InfoBanner() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = BackgroundGray,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Need to make changes?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    "You can modify or cancel your booking up to 30 minutes before the start time.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 14.sp
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

val sampleBookings = listOf(
    Booking(
        id = "BKG12345",
        stationName = "GreenCharge Station",
        location = "Bandra Kurla Complex, Mumbai",
        connectorType = "CCS2",
        connectorPower = "50 kW",
        date = "24 May 2025",
        weekday = "Saturday",
        timeRange = "10:00 AM - 11:00 AM",
        durationText = "1 hour",
        price = "₹120.00",
        status = "Upcoming",
        isDC = true
    ),
    Booking(
        id = "BKG12346",
        stationName = "VoltPoint Hub",
        location = "Powai, Mumbai",
        connectorType = "Type 2",
        connectorPower = "22 kW",
        date = "25 May 2025",
        weekday = "Sunday",
        timeRange = "02:30 PM - 04:00 PM",
        durationText = "1.5 hours",
        price = "₹60.00",
        status = "Upcoming",
        isDC = false
    ),
    Booking(
        id = "BKG12347",
        stationName = "ChargeZone DC Fast",
        location = "Navi Mumbai, Maharashtra",
        connectorType = "CCS2",
        connectorPower = "60 kW",
        date = "27 May 2025",
        weekday = "Tuesday",
        timeRange = "09:00 AM - 10:00 AM",
        durationText = "1 hour",
        price = "₹180.00",
        status = "Upcoming",
        isDC = true
    )
)

private val BackgroundGray = Color(0xFFF8F8F8)

@Preview(showBackground = true)
@Composable
fun UpcomingBookingsScreenPreview() {
    EVSpotTheme {
        UpcomingBookingsScreen()
    }
}
