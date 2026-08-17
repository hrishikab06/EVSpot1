# Walkthrough - Plan a Trip Map Layout & UI Restoration

I have restored the **Plan a Trip** screen to its original UI structure (Reference 1) while successfully implementing the expanded map layout (Reference 2) and real-world route functionality.

## Changes Made

### 1. `PlanTripScreen.kt`
*   **Sticky Bottom Bar**: Restored the "Save Trip" and "Start Navigation" buttons to a fixed position at the bottom of the screen using the `Scaffold`'s `bottomBar` parameter.
*   **Expanded Map Layout**: Refactored the main content area to use a `BottomSheetScaffold`.
    *   The map now serves as a large background layer, filling the available space.
    *   The `LocationInputCard` and `TripFilterChips` float elegantly over the top of the map.
    *   The trip overview and route details are now contained within a draggable bottom sheet with a visible handle.
*   **Interaction Logic**:
    *   The destination field is now fully clickable and triggers a Places Autocomplete search.
    *   Selecting a destination updates the map with a red marker, calculates the real driving route (green polyline), and identifies charging stations along the path.
    *   The "Total Distance" and "Total Time" in the Trip Overview update dynamically based on the actual route results.

### 2. Map Components (`MapScreen.kt` & `ChargingMap.kt`)
*   Ensured the current location marker uses the EVSpot Green hue.
*   Implemented rendering for the route polyline and destination marker.
*   The camera now automatically fits the entire route on the screen when a destination is selected.

## Verification Results
*   **Layout Fidelity**: The bottom buttons are fixed, and the map follows the expanded layout shown in Reference 2.
*   **Functionality**: Search, routing, and charging station detection are all live and functional.
*   **Build**: `gradlew :app:assembleDebug` completed successfully.
