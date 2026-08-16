# Implementation Plan - Plan a Trip UI Restoration & Functionality

This plan restores the original UI layout of the **Plan a Trip** screen (as per REFERENCE 1) while adding real-world map and trip planning functionality, including destination search, route calculation, and charging stations along the route.

## Proposed Changes

### Core UI Structure

#### [MODIFY] [PlanTripScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/PlanTripScreen.kt)
*   **Sticky Bottom Bar**: Restructure the root layout to use a standard `Scaffold` with `bottomBar = { BottomActionButtons() }`. This ensures the buttons remain fixed and don't scroll with the content.
*   **Main Content**: Use a `Column` for the scrollable content above the fixed bottom bar.
*   **Interactive Input**:
    *   Update `LocationInputCard` to use `TextField` or clickable areas for "Current Location" and "Destination".
    *   Implement a full-screen or overlay UI for Places Autocomplete suggestions when typing in the destination field.
*   **State Management**:
    *   `origin`: LatLng (defaults to device location).
    *   `destination`: LatLng? (selected via Autocomplete).
    *   `routePoints`: List<LatLng> (calculated polyline).
    *   `nearbyChargingStations`: List<ChargingSpot> (found along the route).
*   **Map Integration**: Update `ChargingMap` calls to pass the `routePolyline` and `destination` marker.

### Data & Repositories

#### [MODIFY] [PlacesRepository.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/PlacesRepository.kt)
*   Add `fetchAutocompleteSuggestions(query: String)`: Fetches suggestions as the user types.
*   Add `fetchPlaceDetails(placeId: String)`: Retrieves coordinates for the selected suggestion.
*   Add `searchAlongRoute(path: List<LatLng>)`: A strategy to find charging stations along the route path (e.g., searching at 20km intervals or using a bounding box).

#### [NEW] `RouteRepository.kt`
*   Implement `fetchRoute(origin: LatLng, destination: LatLng)`: Calls Google Directions API to get route geometry and metadata (distance, duration).

### Map Components

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   Add support for rendering a `Polyline` when `routePoints` are provided.
*   Show a destination marker when a location is selected.

## Verification Plan

### Automated Tests
*   Run `gradlew :app:assembleDebug` to verify the build.

### Manual Verification (against REFERENCE 1)
1.  **Layout**: Confirm the "Save Trip" and "Start Navigation" buttons are fixed at the bottom.
2.  **Search**: Tap the destination field, type a place, and select a suggestion.
3.  **Route**: Verify a polyline appears on the map connecting the user to the destination.
4.  **Stations**: Verify charging station markers appear along the route.
5.  **Overview**: Confirm the "Total Distance" and "Total Time" update based on the actual route.
