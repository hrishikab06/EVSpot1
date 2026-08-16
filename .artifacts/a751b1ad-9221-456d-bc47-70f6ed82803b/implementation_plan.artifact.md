# Implementation Plan - Nearby Stations Layout Fix

This plan addresses the layout compression and squeezing issues in the `Nearby Stations` screen while preserving the existing UI design.

## User Review Required

> [!IMPORTANT]
> This fix only modifies the layout constraints (weights and widths) of the existing `StationCard` to ensure it uses the screen width correctly and prevents text squeezing. No visual design changes are being made.

## Proposed Changes

### UI Components

#### [MODIFY] [NearbyChargersScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/NearbyChargersScreen.kt)

*   **`StationCard` Refactoring (Layout Constraints only):**
    *   In the top `Row` of the card, add `Modifier.weight(1f)` to the left `Column` (containing the station name, location, and distance). This ensures the left column takes up all available space and correctly wraps text, while pushing the right column (availability, price, rating) to the end without being squeezed.
    *   Add a small end padding to the left `Column` to prevent text from touching the right-side components.
    *   In the bottom `Row` of the card, add `Modifier.weight(1f)` to the inner `Row` (containing power, connectors, and hours). This prevents the "View Details" button from being pushed off-screen or squeezed by long metadata text.
    *   Ensure all `Text` components use idiomatic wrapping behavior by removing any unintended width constraints.

*   **`NearbyStationsSheetContent` Verification:**
    *   Confirm the `LazyColumn` and `StationCard` correctly fill the maximum width provided by the `BottomSheetScaffold`.

## Verification Plan

### Automated Tests
*   Run `gradlew :app:assembleDebug` to verify the build.

### Manual Verification
*   Open the "Find Nearby" screen.
*   Verify that charging station cards occupy the full width of the screen (minus standard margins).
*   Verify that long station names and addresses wrap correctly over multiple lines instead of squeezing into a narrow column.
*   Verify that the price, rating, and availability information on the right side of the card are correctly aligned to the end and not overlapping with the station name.
*   Verify that the "View Details" button is clearly visible and correctly positioned at the bottom-right of the card.
*   Confirm that the map and Places API functionality are still active and working.
