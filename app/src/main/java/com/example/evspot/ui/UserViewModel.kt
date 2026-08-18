package com.example.evspot.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.evspot.model.Booking
import com.example.evspot.model.ChargingSession
import com.example.evspot.model.PastTripItem
import com.example.evspot.model.sampleBookings
import com.example.evspot.model.completedBookings
import com.example.evspot.model.cancelledBookings
import com.example.evspot.model.samplePastTrips
import com.example.evspot.model.sampleSessions
import com.example.evspot.model.sampleNotifications

import com.example.evspot.model.NotificationItem

class UserViewModel : ViewModel() {
    private val _notifications = mutableStateListOf<NotificationItem>().apply {
        addAll(sampleNotifications)
    }
    val notifications: List<NotificationItem> = _notifications

    private val _bookings = mutableStateListOf<Booking>().apply {
        addAll(sampleBookings)
    }
    val bookings: List<Booking> = _bookings

    private val _completedBookings = mutableStateListOf<Booking>().apply {
        addAll(com.example.evspot.model.completedBookings)
    }
    val completedBookings: List<Booking> = _completedBookings

    private val _cancelledBookings = mutableStateListOf<Booking>().apply {
        addAll(com.example.evspot.model.cancelledBookings)
    }
    val cancelledBookings: List<Booking> = _cancelledBookings

    private val _sessions = mutableStateListOf<ChargingSession>().apply {
        addAll(sampleSessions)
    }
    val sessions: List<ChargingSession> = _sessions

    private val _pastTrips = mutableStateListOf<PastTripItem>().apply {
        addAll(samplePastTrips)
    }
    val pastTrips: List<PastTripItem> = _pastTrips

    fun addBooking(booking: Booking) {
        _bookings.add(0, booking)
    }

    fun cancelBooking(bookingId: String) {
        _bookings.removeIf { it.id == bookingId }
    }
}
