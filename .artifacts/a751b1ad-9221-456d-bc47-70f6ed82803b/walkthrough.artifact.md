# Walkthrough - Nearby Stations Layout Fix

I have fixed the layout compression issues in the `Nearby Stations` screen. The charging station cards now use the full available screen width correctly, and text wraps as expected without squeezing the metadata or buttons.

## Changes Made

### 1. `NearbyChargersScreen.kt`
*   **StationCard Layout Fixes**:
    *   Added `Modifier.weight(1f)` to the left-side `Column` in the main card row. This ensures the station name, location, and distance information take up the available horizontal space and wrap properly, preventing the "squeezed" appearance.
    *   Added `Modifier.weight(1f, fill = false)` to the station name `Text` component to handle long names gracefully while keeping the type badge visible.
    *   Added `Modifier.weight(1f)` to the metadata `Row` (power, connectors, hours) to ensure the "View Details" button is correctly positioned at the end of the card and not pushed off-screen.
    *   Used `Modifier.alignByBaseline()` for the metadata text components to ensure consistent vertical alignment across different font sizes.
    *   Added `PaddingValues` to the "View Details" button to ensure it maintains its proportion while occupying the correct space.
    *   Preserved all existing visual styles, colors, icons, and typography as per the reference design.

## Verification Results
*   **Build**: `gradlew :app:assembleDebug` completed successfully.
*   **Functionality**:
    *   The map functionality (current location, Places API results, markers, search radius) remains fully intact.
    *   The bottom sheet interaction and expandable map behavior are preserved.
    *   The layout is now responsive and handles long text strings without vertical character wrapping.
