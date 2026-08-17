package com.example.evspot.model

enum class SessionStatus { COMPLETED, CANCELLED }

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

data class ChargingSession(
    val stationName: String,
    val location: String,
    val dateTime: String,
    val price: Int,
    val status: SessionStatus,
    val batteryFrom: Int? = null,
    val batteryTo: Int? = null,
    val energyKwh: Double? = null,
    val durationMinutes: Int? = null
)

data class PastTripItem(
    val startLocation: String,
    val startDate: String,
    val destination: String,
    val endDate: String,
    val distance: String,
    val duration: String
)

val sampleBookings = listOf(
    Booking(
        id = "BKG98210",
        stationName = "BKC EV Hub",
        location = "G Block, BKC, Mumbai",
        connectorType = "CCS2",
        connectorPower = "120 kW",
        date = "17 Aug 2026",
        weekday = "Monday",
        timeRange = "10:00 AM - 11:00 AM",
        durationText = "1 hour",
        price = "₹220.00",
        status = "Upcoming",
        isDC = true
    ),
    Booking(
        id = "BKG98211",
        stationName = "Powai Charging Point",
        location = "Central Ave, Hiranandani, Mumbai",
        connectorType = "Type 2",
        connectorPower = "22 kW",
        date = "18 Aug 2026",
        weekday = "Tuesday",
        timeRange = "02:30 PM - 04:00 PM",
        durationText = "1.5 hours",
        price = "₹90.00",
        status = "Upcoming",
        isDC = false
    ),
    Booking(
        id = "BKG98212",
        stationName = "Navi Mumbai Fast Charge",
        location = "Vashi Sector 17, Navi Mumbai",
        connectorType = "CCS2",
        connectorPower = "60 kW",
        date = "20 Aug 2026",
        weekday = "Thursday",
        timeRange = "09:00 AM - 10:00 AM",
        durationText = "1 hour",
        price = "₹180.00",
        status = "Upcoming",
        isDC = true
    )
)

val completedBookings = listOf(
    Booking(
        id = "BKG98205",
        stationName = "Worli Seaface Charger",
        location = "Worli, Mumbai",
        connectorType = "CCS2",
        connectorPower = "150 kW",
        date = "12 Aug 2026",
        weekday = "Wednesday",
        timeRange = "08:30 PM - 09:30 PM",
        durationText = "1 hour",
        price = "₹312.00",
        status = "Completed",
        isDC = true
    ),
    Booking(
        id = "BKG98200",
        stationName = "Marine Drive Point",
        location = "Marine Drive, Mumbai",
        connectorType = "CCS2",
        connectorPower = "50 kW",
        date = "10 Aug 2026",
        weekday = "Monday",
        timeRange = "06:15 PM - 07:45 PM",
        durationText = "1.5 hours",
        price = "₹454.00",
        status = "Completed",
        isDC = true
    )
)

val cancelledBookings = listOf(
    Booking(
        id = "BKG98190",
        stationName = "Thane EV Station",
        location = "Viviana Mall, Thane",
        connectorType = "CCS2",
        connectorPower = "60 kW",
        date = "08 Aug 2026",
        weekday = "Saturday",
        timeRange = "07:45 PM - 08:45 PM",
        durationText = "1 hour",
        price = "₹0.00",
        status = "Cancelled",
        isDC = true
    )
)

val sampleSessions = listOf(
    ChargingSession("Worli Seaface Charger", "Worli, Mumbai", "15 Aug 2026, 08:30 PM", 312, SessionStatus.COMPLETED, 20, 82, 18.4, 72),
    ChargingSession("Marine Drive Point", "Marine Drive, Mumbai", "12 Aug 2026, 06:15 PM", 454, SessionStatus.COMPLETED, 35, 90, 22.7, 88),
    ChargingSession("Andheri Fast Point", "Andheri East, Mumbai", "10 Aug 2026, 10:05 AM", 284, SessionStatus.COMPLETED, 15, 70, 16.2, 65),
    ChargingSession("Thane EV Station", "Viviana Mall, Thane", "08 Aug 2026, 07:45 PM", 0, SessionStatus.CANCELLED),
    ChargingSession("BKC EV Hub", "G Block, BKC, Mumbai", "05 Aug 2026, 09:20 PM", 366, SessionStatus.COMPLETED, 22, 78, 19.6, 80)
)

val samplePastTrips = listOf(
    PastTripItem("Mumbai", "Aug 12, 2026 • 08:30 AM", "Pune", "Aug 12, 2026 • 11:45 AM", "148 km", "3h 15m"),
    PastTripItem("Pune", "Aug 10, 2026 • 09:20 AM", "Lonavala", "Aug 10, 2026 • 01:50 PM", "65 km", "1h 30m"),
    PastTripItem("Mumbai", "Aug 08, 2026 • 06:40 PM", "Alibaug", "Aug 08, 2026 • 09:40 PM", "95 km", "3h 00m"),
    PastTripItem("Mumbai", "Aug 06, 2026 • 10:00 AM", "Nashik", "Aug 06, 2026 • 01:30 PM", "167 km", "3h 30m"),
    PastTripItem("Vashi", "Aug 04, 2026 • 08:15 AM", "Panvel", "Aug 04, 2026 • 08:45 AM", "20 km", "0h 30m")
)
