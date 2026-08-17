package com.example.evspot.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType
)

enum class NotificationType {
    BOOKING, BATTERY, SYSTEM
}

val sampleNotifications = listOf(
    NotificationItem(
        "1",
        "Booking Confirmed",
        "Your booking at BKC EV Hub for 10:00 AM tomorrow is confirmed.",
        "2 hours ago",
        NotificationType.BOOKING
    ),
    NotificationItem(
        "2",
        "Low Battery Alert",
        "Your EvSpot EV-01 battery is at 10%. Please charge soon.",
        "5 hours ago",
        NotificationType.BATTERY
    ),
    NotificationItem(
        "3",
        "Charging Completed",
        "Your EvSpot Scooter has finished charging at Worli Seaface.",
        "Yesterday",
        NotificationType.BATTERY
    ),
    NotificationItem(
        "4",
        "New Hub Near You",
        "A new Superfast charging hub just opened in Bandra East!",
        "2 days ago",
        NotificationType.SYSTEM
    )
)
