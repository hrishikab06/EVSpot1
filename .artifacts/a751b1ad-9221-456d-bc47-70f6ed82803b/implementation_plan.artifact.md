# Implementation Plan - Nearby Charging Recommendation Algorithm

This plan implements a deterministic scoring and ranking algorithm for the Nearby Charging Stations feature, including user-selectable filters and map highlights for recommended stations.

## User Review Required

> [!IMPORTANT]
> Google Places API (New) has limited support for EV-specific data like charging speed or per-kWh cost. The algorithm will prioritize available metrics (Distance, Rating, Price Level) and handle missing fields gracefully without inventing fake data.

## Proposed Changes

### Data Models

#### [MODIFY] [ChargingSpot.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/model/ChargingSpot.kt)
*   Add `rating: Double?`, `userRatingsTotal: Int?`, and `priceLevel: Int?` to the data class.

### Repositories

#### [MODIFY] [PlacesRepository.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/PlacesRepository.kt)
*   Update `searchChargingStations` and `searchAlongRoute` to request `RATING`, `USER_RATING_COUNT`, and `PRICE_LEVEL` fields from the Places SDK.
*   Populate the new fields in the `ChargingSpot` objects.

### UI Components

#### [MODIFY] [MapScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
*   Add `highlightedSpotId: String?` parameter.
*   Update marker rendering logic: if a spot matches the `highlightedSpotId`, set its marker color to Azure (Blue).

#### [MODIFY] [ChargingMap.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/components/ChargingMap.kt)
*   Add `highlightedSpotId: String?` and pass it down to `MapScreen`.

#### [MODIFY] [NearbyChargersScreen.kt](file:///C:/Users/deepika/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/NearbyChargersScreen.kt)
*   **Ranking Logic**: Implement `calculateEvSpotScore` based on Distance (40%), Cost/PriceLevel (25%), Rating (15%), etc.
*   **Filter UI**: Add a horizontal scrollable row of compact filter chips (Recommended, Nearest, Cheapest, Fastest, Rating) with Material icons.
*   **State Management**: Track the selected filter and re-rank the `chargerStations` list immediately.
*   **Map Integration**: Identify the top-ranked station for the selected filter and pass its ID as the `highlightedSpotId` to the map.

## Verification Plan

### Automated Tests
*   Run `gradlew :app:assembleDebug` to ensure the project builds successfully.

### Manual Verification
1.  **Filter Selection**: Open "Find Nearby" and tap different filters. Verify the list reorders instantly.
2.  **Map Highlights**: Verify that only the top-ranked station for the active filter is colored BLUE on the map.
3.  **Accuracy**: Verify "Nearest" actually puts the closest station at the top.
4.  **Graceful Handling**: Ensure stations with missing ratings or price levels are still rankable and don't cause crashes.
5.  **Isolation**: Confirm that "Plan a Trip" and other screens remain unchanged.
