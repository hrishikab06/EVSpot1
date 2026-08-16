# Walkthrough - Vehicle Marker Integration

I have extended the map implementation to display a visually distinct vehicle marker at the user's current location across all map-enabled screens.

## Changes Made

### 1. `MapConfig.kt`
*   Removed the hardcoded `vehicleLocation` to favor dynamic location sources.

### 2. `MapScreen.kt`
*   Added `vehicleLocation: LatLng?` parameter to allow for backend GPS overrides in the future.
*   Added `deviceLocation` state to track the real-time position of the device.
*   Updated the vehicle marker to use `vehicleLocation` if provided, otherwise `deviceLocation`, with a final fallback to Singapore.
*   The vehicle marker uses `BitmapDescriptorFactory.HUE_AZURE` to distinguish it from red charging station markers.

### 3. `ChargingMap.kt`
*   Exposed `vehicleLocation` parameter and passed it down to `MapScreen`.

### 4. `NearbyChargersScreen.kt` & `PlanTripScreen.kt`
*   Replaced the "Map goes here" and "Map Preview" placeholders with the live `ChargingMap` component.
*   This ensures the vehicle marker and "My Location" functionality are consistent across the entire app.

## Verification Results
*   **Build**: `gradlew :app:assembleDebug` finished successfully.
*   **Data Separation**: The vehicle marker logic is decoupled from the UI, accepting coordinates as optional parameters.
*   **Visual Distinction**: The Azure vehicle marker stands out clearly from the standard red markers.
*   **Reuse**: The unified `MapScreen` implementation is now used in all three primary map surfaces (Home, Nearby, Trip Planner).
