# Walkthrough - Nearby Charging Recommendation Algorithm

I have implemented a deterministic ranking algorithm for the **Nearby Charging Stations** feature, allowing users to re-order results based on various real-world metrics.

## Key Implementation Details

### 1. Recommendation Algorithm
Created a weighted scoring system in `NearbyChargersScreen.kt` that calculates an **EVSpot Score** for each station.
*   **Distance (40%)**: Prioritizes stations closest to the active search center.
*   **Cost Indicator (25%)**: Uses Google's `priceLevel` to rank affordability.
*   **User Rating (15%)**: Based on average user stars.
*   **Reliability (10%)**: Weighted by the total number of user ratings.
*   **Suitability (10%)**: Provides a baseline for future extension with more EV data.

### 2. User-Selectable Filters
Added a modern, scrollable row of filter chips at the top of the station list:
*   ⭐ **Recommended**: The default mode using the weighted EVSpot score.
*   📍 **Nearest**: Sorts by physical distance.
*   ₹ **Cheapest**: Sorts by price level (indicates "Price N/A" if unknown).
*   ⚡ **Fastest**: Prepared for power data (indicates "Power N/A" currently).
*   ★ **Rating**: Sorts by average user reviews.

### 3. Visual Map Highlights
*   The top-ranked station for any selected filter is automatically highlighted on the map with a distinctive **Azure (Blue)** marker.
*   All other stations remain on the map in their original locations to maintain context.
*   The highlight moves instantly as you switch between filters.

### 4. Data Reliability
*   **Real Data**: Stations are fetched from Google Places with real ratings and price levels.
*   **No Fabricated Values**: If Google does not provide a cost or rating, the algorithm handles it gracefully with a neutral score instead of creating fake data.

## Verification Results
*   **Build**: `gradlew :app:assembleDebug` completed successfully.
*   **Functionality**: Reordering the list updates the map marker highlight in real-time.
*   **Isolation**: No changes were made to "Plan a Trip" or the Home page, keeping those experiences independent.
