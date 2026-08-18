# EVSpot Backend Integration Plan

Connect the existing Android frontend to the FastAPI backend while preserving the station-selection algorithm and UI identity.

## User Review Required

> [!IMPORTANT]
> The navigation route for `StationDetailScreen` will be changed from `stationName` to `stationId`. This ensures we pull the correct data from the backend.

## Proposed Changes

### [Data Layer]

#### [NEW] [ApiModels.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/api/ApiModels.kt)
Define data classes matching the FastAPI backend responses for Stations, Chargers, Bookings, and Availability.

#### [MODIFY] [ApiService.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/api/ApiService.kt)
Add the following endpoints:
- `GET /stations`
- `GET /stations/{stationId}`
- `GET /stations/{stationId}/chargers`
- `POST /chargers/{chargerId}/availability`
- `POST /bookings`

#### [NEW] [StationRepository.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/StationRepository.kt)
Handle network requests and provide a clean API for ViewModel interaction.

---

### [UI Layer]

#### [MODIFY] [UserViewModel.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/UserViewModel.kt)
- Add `userId`, `userEmail`, and `userName` states.
- Implement `setUserSession` to be called after login.

#### [MODIFY] [LoginScreen.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/auth/LoginScreen.kt)
- Update login logic to call `userViewModel.setUserSession` upon success.

#### [MODIFY] [NearbyChargersScreen.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/NearbyChargersScreen.kt)
- Fetch real stations from `GET /stations`.
- Map backend `Station` objects to `ChargerStation` (preserving existing model if possible) or update the list logic.
- Ensure the existing `calculateEvSpotScore` algorithm is applied to the real data.
- Pass `station.id` to the navigation route.

#### [MODIFY] [StationDetailScreen.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/StationDetailScreen.kt)
- Change parameter from `stationName` to `stationId`.
- Fetch real station details from `GET /stations/{stationId}`.
- Fetch all chargers for the station from `GET /stations/{stationId}/chargers`.
- Implement charging time selection and availability check (`POST /chargers/{chargerId}/availability`).
- Implement the booking flow (`POST /bookings`).
- Display the real booking confirmation.

#### [MODIFY] [NavGraph.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/navigation/NavGraph.kt) & [Screen.kt](file:///home/hrishi/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/navigation/Screen.kt)
- Update `StationDetail` route to use `stationId` as an integer argument.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the build.

### Manual Verification
1. Log in and verify the user ID is captured.
2. Open "Nearby Chargers" and verify ~270 stations are fetched (or a subset if paged, though API seems to return all).
3. Select a station and verify the ID is passed to `StationDetailScreen`.
4. Verify all chargers (CCS2, CHAdeMO, etc.) are displayed with correct power.
5. Select a time and verify availability check.
6. Complete a booking and verify the confirmation details (ID, deadline).
7. Attempt an overlapping booking and verify the conflict message.
