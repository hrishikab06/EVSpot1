# Google Maps API Integration & Charging Stations Plan

This plan outlines the integration of the Google Maps API key using the `secrets-gradle-plugin` and the implementation of vehicle/charging station markers on the map.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/mihirjagtap/StudioProjects/EVSpot1/gradle/libs.versions.toml)
- Add `secrets-gradle-plugin` to the `plugins` and `versions` sections.
- Add `google-places` to the `libraries` section.

#### [MODIFY] [build.gradle.kts](file:///Users/mihirjagtap/StudioProjects/EVSpot1/build.gradle.kts)
- Add the `secrets-gradle-plugin` to the top-level build script.

#### [MODIFY] [app/build.gradle.kts](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/build.gradle.kts)
- Apply the `secrets-gradle-plugin`.
- Add the `play-services-places` (or `places` SDK) dependency.

### Manifest & Secrets

#### [MODIFY] [AndroidManifest.xml](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/AndroidManifest.xml)
- Replace the hardcoded API key with the `${MAPS_API_KEY}` placeholder.

#### [NEW] [local.properties](file:///Users/mihirjagtap/StudioProjects/EVSpot1/local.properties)
- Add `MAPS_API_KEY=AIzaSyBpj8re2JNJiLAUZ4ePYctVNMp7QyxcMfU` (using the previously provided key).

### Map Implementation

#### [NEW] [MapConfig.kt](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/model/MapConfig.kt)
- Create a data class/object to hold configurable dummy coordinates for the vehicle and search radius.

#### [MODIFY] [MapScreen.kt](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/MapScreen.kt)
- Add a marker for the dummy vehicle location using a custom icon or specific color.
- Integrate (or structure for) Google Places API to fetch nearby charging stations.
- Ensure the map centers on the vehicle location by default.

#### [MODIFY] [ChargingMap.kt](file:///Users/mihirjagtap/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/components/ChargingMap.kt)
- Set `useMock = false` as the default to enable the real Google Maps view now that the API key is integrated.

## Verification Plan

### Automated Tests
- Build the project using `./gradlew assembleDebug` to ensure the `secrets-gradle-plugin` correctly injects the API key.

### Manual Verification
- Deploy to an Android device/emulator.
- Verify that the Google Map loads (no "Map Preview Mode" overlay).
- Verify the vehicle marker is displayed at the configured coordinates.
- Verify charging station markers are visible.
