# Live BMS Simulation with Dataset

We will integrate a CSV-based simulation to provide live BMS (Battery Management System) updates to the `VehicleHealthScreen`.

## Proposed Changes

### [Component] Assets
#### [NEW] [range_data.csv](file:///C:/Users/dutta/StudioProjects/EVSpot1/app/src/main/assets/range_data.csv)
Copying the provided CSV file to the assets directory for runtime access.

### [Component] Data Model
#### [NEW] [BatteryStatus.kt](file:///C:/Users/dutta/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/model/BatteryStatus.kt)
Defining the `BatteryStatus` data class to hold the BMS values.

### [Component] Data Repository
#### [NEW] [BatteryRepository.kt](file:///C:/Users/dutta/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/data/BatteryRepository.kt)
Creating a repository to parse the CSV from assets.

### [Component] ViewModel
#### [MODIFY] [VehicleViewModel.kt](file:///C:/Users/dutta/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/VehicleViewModel.kt)
Updating the ViewModel to stream `BatteryStatus` updates using a coroutine and `StateFlow`.

### [Component] UI
#### [MODIFY] [VehicleHealthScreen.kt](file:///C:/Users/dutta/StudioProjects/EVSpot1/app/src/main/java/com/example/evspot/ui/screens/detail/VehicleHealthScreen.kt)
Updating the screen to subscribe to the live stream and adding the `BmsStatusCard`.

## Verification Plan

### Automated Tests
- None planned for this UI simulation.

### Manual Verification
- Deploy the app and navigate to `VehicleHealthScreen`.
- Observe if the SOC, temperature, and range values update periodically.
