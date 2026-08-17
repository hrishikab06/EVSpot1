# Home Page UI & Interaction Corrections

This plan addresses the UI polish of the search bar, its visibility during map expansion, and removes duplicate/unwanted map visual elements.

## Proposed Changes

### UI Components

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   **Zoom Controls**: Explicitly set `zoomControlsEnabled = false` in `MapUiSettings` to remove the default Google Maps buttons, leaving only the EVSpot custom ones.
*   **Radius Circle**: Adjust logic to render the `Circle` **only** when `searchCenter` is not null. This removes the range circle from the current location on initial load.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/HomeScreen.kt)
*   **Visibility Logic**: Observe `scaffoldState.bottomSheetState` and hide the top bar/search bar when the bottom sheet is expanded.
*   **Modern Search Bar**:
    *   Update the search field within `FloatingTopBar` to use a modern pill-shaped design (`RoundedCornerShape(28.dp)`).
    *   Improve padding, typography, and background consistency with EVSpot's visual style.
    *   Ensure the transition (hiding/showing) is smooth.

## Verification Plan

### Automated Tests
*   Run `gradlew :app:assembleDebug` to verify the build.

### Manual Verification
1.  **Search Bar Style**: Verify the new search bar looks modern and matches the EVSpot UI.
2.  **Expansion Behavior**: Drag the bottom panel up. Verify the search bar disappears smoothly.
3.  **Zoom Controls**: Verify only one set of zoom buttons is visible on the map.
4.  **Radius Circle**:
    *   Verify NO circle is visible around the current location initially.
    *   Search for a place. Verify the 5 km circle appears ONLY around that searched point.
5.  **Functionality**: Confirm search, current location, and markers still work as expected.
