# EVSpot - Electric Vehicle Charging Assistant

EVSpot is a sophisticated Android application designed to help electric vehicle (EV) users find, evaluate, and reserve charging slots at stations across India. Unlike simple directory apps, EVSpot provides real-time data integration and a secure booking cycle.

## 🚀 Key Features

*   **Real-time Station Discovery**: Fetches real charging station data (~270 stations) from a live FastAPI backend.
*   **Intelligent Station Scoring**: Uses a specialized algorithm to rank stations based on distance, rating, price, and suitability for the user's vehicle.
*   **Proximity Filtering**: Automatically filters and displays stations within a **10 km radius** of the user's current location or search center.
*   **Detailed Station Insights**: View station operator information, opening hours, amenities, and accurate GPS coordinates.
*   **Charger Management**: Lists all individual charging points at a station, including connector types (CCS2, CHAdeMO, etc.) and power output (kW).
*   **Immediate Booking**:
    *   Real-time availability check before reservation.
    *   Immediate slot booking starting from the current time.
    *   Booking confirmation with arrival deadlines (typically 10 minutes) and unique IDs.
*   **User Authentication**: Integrated Login and Sign Up flow to manage sessions and booking history.
*   **BMS Simulation**: Real-time vehicle data simulation (SOC, range, temperature) to provide accurate charging recommendations.

## 🛠 Tech Stack

### Frontend (Android)
*   **UI**: Jetpack Compose (Material 3)
*   **Networking**: Retrofit & OkHttp
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Navigation**: Jetpack Navigation Component
*   **Maps**: Google Maps SDK & Google Places SDK
*   **Architecture**: MVVM (Model-View-ViewModel)

### Backend
*   **Framework**: FastAPI (Python)
*   **Database**: PostgreSQL
*   **Deployment**: AWS EC2
*   **Integration**: OCPI (Open Charge Point Interface) support in progress.

## 📂 Project Structure

*   `app/src/main/java/com/example/evspot/data`: Contains repositories and API service definitions.
*   `app/src/main/java/com/example/evspot/model`: Data classes and configuration objects.
*   `app/src/main/java/com/example/evspot/ui/screens`: Feature-specific Compose screens (Nearby, Detail, Booking, Auth).
*   `app/src/main/java/com/example/evspot/navigation`: Centralized navigation graph and route definitions.

## 📡 API Integration

The app is connected to the production backend at: `http://13.61.188.154:8000/`

**Authoritative Endpoints Used:**
- `GET /stations`: Fetch all stations.
- `GET /stations/{id}`: Specific station details.
- `GET /stations/{id}/chargers`: Real-time charger list and availability.
- `POST /chargers/{id}/availability`: Future-window availability verification.
- `POST /bookings`: Secure slot reservation.

## ⚖️ License
Internal Project - All rights reserved.
