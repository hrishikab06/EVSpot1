# Fix Current-Location Behavior for Google Maps

The current map implementation in EVSpot is hardcoded to a fixed location in Singapore. This plan outlines the steps to integrate real device location tracking, runtime permission handling, and initial camera centering on the user's location while maintaining the existing UI.

## User Review Required

> [!IMPORTANT]
> The app will now request `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions when the map screens are first accessed. If denied, the map will fall back to the existing Singapore location to ensure the app remains functional.

## Proposed Changes

### UI Components

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   Add `FusedLocationProviderClient` to fetch the device's current location.
*   Update `MapScreen` signature to accept a `CameraPositionState`.
*   Implement `LaunchedEffect` to fetch the initial location and move the camera *once*.
*   Ensure `isMyLocationEnabled` and `myLocationButtonEnabled` (UI settings) are synced with permission status.

#### [MODIFY] [ChargingMap.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/components/ChargingMap.kt)
*   Create a `CameraPositionState` to share with `MapScreen`.
*   Wire up the `MyLocation` Floating Action Button (FAB) to animate the camera to the current location using `FusedLocationProviderClient`.
*   Handle FAB clicks with a coroutine to ensure smooth transitions.

### Models

#### [MODIFY] [MapConfig.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/model/MapConfig.kt)
*   Keep the Singapore location as `DEFAULT_LOCATION` fallback.

## Verification Plan

### Automated Tests
*   Run `gradlew assembleDebug` to ensure no compilation errors.

### Manual Verification
*   Deploy to a device/emulator.
*   Verify location permission dialog appears.
*   Verify map centers on current location upon first load.
*   Verify "My Location" FAB centers the map on the blue dot.
*   Verify that denying permissions does not crash the app and shows the fallback location.
