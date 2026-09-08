package com.example.evspot.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evspot.model.Booking
import com.example.evspot.model.ChargingSession
import com.example.evspot.model.PastTripItem
import com.example.evspot.model.samplePastTrips
import com.example.evspot.model.sampleSessions
import com.example.evspot.model.NotificationItem
import com.example.evspot.model.sampleNotifications
import com.example.evspot.data.StationRepository
import com.example.evspot.data.api.UserBooking
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserViewModel : ViewModel() {
    private val repository = StationRepository()

    var userId by mutableStateOf<Int?>(null)
        private set

    var userEmail by mutableStateOf<String?>(null)
        private set

    var userName by mutableStateOf<String?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun setUserSession(id: Int, email: String, name: String) {
        userId = id
        userEmail = email
        userName = name
        fetchBookings()
    }

    private val _notifications = mutableStateListOf<NotificationItem>().apply {
        addAll(sampleNotifications)
    }
    val notifications: List<NotificationItem> = _notifications

    private val _bookings = mutableStateListOf<Booking>()
    val bookings: List<Booking> = _bookings

    private val _completedBookings = mutableStateListOf<Booking>()
    val completedBookings: List<Booking> = _completedBookings

    private val _cancelledBookings = mutableStateListOf<Booking>()
    val cancelledBookings: List<Booking> = _cancelledBookings

    private val _sessions = mutableStateListOf<ChargingSession>().apply {
        addAll(sampleSessions)
    }
    val sessions: List<ChargingSession> = _sessions

    private val _pastTrips = mutableStateListOf<PastTripItem>().apply {
        addAll(samplePastTrips)
    }
    val pastTrips: List<PastTripItem> = _pastTrips

    fun fetchBookings() {
        val currentUserId = userId
        if (currentUserId == null) {
            android.util.Log.e("UserViewModel", "fetchBookings failed: userId is null")
            return
        }
        
        android.util.Log.d("UserViewModel", "fetchBookings started for userId: $currentUserId")
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getUserBookings(currentUserId).onSuccess { userBookings ->
                android.util.Log.d("UserViewModel", "fetchBookings success: received ${userBookings.size} bookings")
                _bookings.clear()
                _completedBookings.clear()
                _cancelledBookings.clear()
                
                userBookings.forEach { ub ->
                    val uiBooking = mapToUiBooking(ub)
                    android.util.Log.d("UserViewModel", "Mapped booking: ID=${ub.id}, Status=${ub.status}, UI_Status=${uiBooking.status}")
                    when (ub.status.lowercase()) {
                        "confirmed", "upcoming", "pending", "booked", "active" -> _bookings.add(uiBooking)
                        "completed", "finished" -> _completedBookings.add(uiBooking)
                        "cancelled", "expired" -> _cancelledBookings.add(uiBooking)
                        else -> {
                            android.util.Log.w("UserViewModel", "Unknown booking status: ${ub.status}")
                            _bookings.add(uiBooking)
                        }
                    }
                }
                isLoading = false
            }.onFailure {
                android.util.Log.e("UserViewModel", "fetchBookings failure: ${it.message}", it)
                errorMessage = it.message ?: "Failed to fetch bookings"
                isLoading = false
            }
        }
    }

    private fun mapToUiBooking(ub: UserBooking): Booking {
        val formats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss"
        )

        fun parseDate(dateStr: String?): Date? {
            if (dateStr == null) return null
            for (format in formats) {
                try {
                    val sdf = SimpleDateFormat(format, Locale.getDefault())
                    return sdf.parse(dateStr)
                } catch (e: Exception) {
                    // try next
                }
            }
            return null
        }

        val dateOutputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val weekdayOutputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val timeOutputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val startDate = parseDate(ub.start_time)
        val endDate = parseDate(ub.end_time)

        val formattedDate = startDate?.let { dateOutputFormat.format(it) } ?: ub.start_time
        val formattedWeekday = startDate?.let { weekdayOutputFormat.format(it) } ?: ""
        val startTimeStr = startDate?.let { timeOutputFormat.format(it) } ?: ""
        val endTimeStr = endDate?.let { timeOutputFormat.format(it) } ?: ""
        
        val durationMillis = if (startDate != null && endDate != null) endDate.time - startDate.time else 0
        val durationHours = durationMillis / (1000 * 60 * 60)
        val durationMinutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
        val durationText = when {
            durationHours > 0 -> "$durationHours hr ${if (durationMinutes > 0) "$durationMinutes min" else ""}"
            else -> "$durationMinutes min"
        }

        return Booking(
            id = ub.id.toString(),
            stationName = ub.station_name ?: "Unknown Station",
            location = ub.station_address ?: "Address not available",
            connectorType = ub.connector_type ?: "CCS2",
            connectorPower = if (ub.power_kw != null) "${ub.power_kw.toInt()} kW" else "50 kW",
            date = formattedDate,
            weekday = formattedWeekday,
            timeRange = "$startTimeStr - $endTimeStr",
            durationText = durationText,
            price = "₹${ub.estimated_cost_inr ?: 0}.00",
            status = ub.status.replaceFirstChar { it.uppercase() },
            isDC = (ub.power_kw ?: 0.0) > 22.0
        )
    }

    fun addBooking(booking: Booking) {
        _bookings.add(0, booking)
    }

    fun cancelBooking(bookingId: String) {
        _bookings.removeIf { it.id == bookingId }
    }
}
