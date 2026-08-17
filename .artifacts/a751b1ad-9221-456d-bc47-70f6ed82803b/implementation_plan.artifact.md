# Plan a Trip UI Restoration & Functionality Fix

This plan restores the original Plan a Trip UI structure (fixed bottom buttons, scrollable overview) while implementing the expanded map layout and core functionality (route calculation, charging stations along route).

## Proposed Changes

### UI Components

#### [MODIFY] [PlanTripScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/PlanTripScreen.kt)
*   **Root Structure**: Wrap the entire screen in a `Scaffold` with `bottomBar = { BottomActionButtons() }` to keep buttons fixed.
*   **Content Area**: Use `BottomSheetScaffold` as the main content.
    *   **Background Content**: A `Box` containing the full-screen `ChargingMap` with `LocationInputCard` and `TripFilterChips` floating at the top.
    *   **Sheet Content**: `TripOverviewCard`, `RoutePlanCard`, etc., in a draggable sheet.
*   **Interactions**: Ensure the destination field is clickable and opens the `SearchOverlay`.
*   **Logic**: Connect `onPlaceSelected` to `RouteRepository` and `PlacesRepository.searchAlongRoute`.

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   Verify the current location marker is green (`HUE_GREEN`).
*   Ensure the route polyline and destination marker are rendered when data is available.

### Data & Logic

#### [MODIFY] [PlacesRepository.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/PlacesRepository.kt)
*   Ensure `searchAlongRoute` is robust and uses the provided path to find relevant stations.

## Verification Plan

### Automated Tests
*   Run `gradlew :app:assembleDebug` to verify the build.

### Manual Verification
1.  **Layout**: Confirm buttons are sticky at the bottom.
2.  **Map**: Verify the map background is large and floating cards are correctly positioned.
3.  **Search**: Tap destination, select a result, and verify the route (polyline) and charging stations appear.
4.  **Overview**: Confirm distance and time reflect the real route.
