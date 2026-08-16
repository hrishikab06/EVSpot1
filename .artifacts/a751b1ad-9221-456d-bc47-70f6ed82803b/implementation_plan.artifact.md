# Implementation Plan - Vehicle Marker Integration

This plan outlines the steps to display the user's EV as a visually distinct vehicle marker on the map, using the device's current location as the source.

## Proposed Changes

### UI Components

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   Modify `MapScreen` to accept an optional `vehicleLocation: LatLng?`.
*   Introduce internal state `deviceLocation` to track the user's current position via `FusedLocationProviderClient`.
*   Update the `Marker` logic:
    *   If `vehicleLocation` is provided (e.g., from a backend later), use it.
    *   Otherwise, if `deviceLocation` is available, use it as the vehicle marker's position.
*   Ensure the vehicle marker is visually distinguishable (e.g., using a specific color or custom icon).
*   Remove the hardcoded fallback to `MapConfig.vehicleLocation` for the vehicle marker.

#### [MODIFY] [NearbyChargersScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/NearbyChargersScreen.kt)
*   Replace "Map goes here" placeholder with `ChargingMap` to reuse the vehicle marker logic.

#### [MODIFY] [PlanTripScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/PlanTripScreen.kt)
*   Replace `MapPlaceholder` with `ChargingMap` to provide consistent vehicle marker functionality.

### Models

#### [MODIFY] [MapConfig.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/model/MapConfig.kt)
*   Clean up `vehicleLocation` if it's no longer needed as a hardcoded value, or repurpose it as a nullable field for clarity.

## Verification Plan

### Automated Tests
*   Run `gradlew assembleDebug` to ensure the project builds successfully.

### Manual Verification
*   Verify that upon granting location permissions, a vehicle marker appears at the current device location.
*   Verify that the vehicle marker looks different from the charging station markers.
*   Ensure the "My Location" blue dot (if enabled) and the Vehicle Marker are coordinated.
