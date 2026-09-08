# Implementation Plan - Complete User Booking Retrieval

Fix the booking retrieval flow by adding the missing `GET /bookings` endpoint to the FastAPI backend and ensuring the Android app correctly parses and displays the real-time data.

## Proposed Changes

### [Backend] FastAPI

#### [MODIFY] [main.py](file:///Users/mihirjagtap/EVSpot_Backend/EVSPot-Backend1/main.py)
- Add a new `GET /bookings` endpoint.
- Filter bookings by `user_id`.
- Implement a SQL join between `bookings`, `chargers`, and `charging_stations` to provide complete details (station name, address, connector type, power) in a single request.
- Ensure proper serialization of `datetime` objects and handle nullable fields like `arrived_at` and `estimated_cost_inr`.

---

### [Android] App

#### [MODIFY] [UserViewModel.kt](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/UserViewModel.kt)
- Improve the `mapToUiBooking` function to be more robust when parsing ISO date strings with varying precision (milliseconds, etc.).
- Ensure `fetchBookings()` is correctly triggered after login and after new booking creation.

#### [MODIFY] [UpcomingBookingsScreen.kt](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/UpcomingBookingsScreen.kt)
- (Already updated in previous step, but verified) Uses `LaunchedEffect` to refresh data on entry and displays loading/error states.

## Verification Plan

### Automated Tests
- Build the Android app: `./gradlew :app:assembleDebug`.

### Manual Verification
1.  **Backend Verification**: Verify the new endpoint exists and returns data for a given `user_id`.
2.  **App Login**: Log in as a user and verify the "Bookings" screen shows a loading spinner.
3.  **Booking Display**: Confirm that previously created bookings appear with correct station names and connector details.
4.  **End-to-End**:
    - Navigate to a station.
    - Create a new booking.
    - Confirm the "Booking Confirmed" dialog appears.
    - Navigate to the "Bookings" screen and verify the new booking is at the top of the "Upcoming" list.
5.  **Multi-user Isolation**: Log in as a different user and verify they cannot see the first user's bookings.
