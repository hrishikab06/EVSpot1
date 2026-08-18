package com.example.evspot.data

import android.content.Context
import android.util.Log
import com.example.evspot.model.ChargingSpot
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.tasks.await

class PlacesRepository(context: Context) {
    private val placesClient: PlacesClient by lazy { Places.createClient(context) }
    private val tag = "PlacesRepository"

    suspend fun getAutocompleteSuggestions(query: String): List<AutocompletePrediction> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        return try {
            val response = placesClient.findAutocompletePredictions(request).await()
            response.autocompletePredictions
        } catch (e: Exception) {
            Log.e(tag, "Error fetching suggestions", e)
            emptyList()
        }
    }

    suspend fun getPlaceLatLng(placeId: String): LatLng? {
        val placeFields = listOf(Place.Field.LOCATION)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        return try {
            val response = placesClient.fetchPlace(request).await()
            response.place.location
        } catch (e: Exception) {
            Log.e(tag, "Error fetching place details", e)
            null
        }
    }

    suspend fun searchChargingStations(
        center: LatLng,
        radiusMeters: Double
    ): List<ChargingSpot> {
        Log.d(tag, "Searching charging stations around: $center with radius: $radiusMeters")
        
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.LOCATION,
            Place.Field.FORMATTED_ADDRESS
        )

        val circle = CircularBounds.newInstance(center, radiusMeters)
        
        val searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(listOf("electric_vehicle_charging_station"))
            .setMaxResultCount(20)
            .build()

        return try {
            val response = placesClient.searchNearby(searchNearbyRequest).await()
            val spots = response.places.map { place ->
                ChargingSpot(
                    id = place.id ?: "",
                    name = place.displayName ?: "Charging Station",
                    position = place.location ?: center,
                    address = place.formattedAddress ?: "Unknown location"
                )
            }
            Log.d(tag, "Found ${spots.size} stations")
            spots.forEach { spot ->
                Log.d(tag, "Station: ${spot.name} at ${spot.position}")
            }
            spots
        } catch (e: Exception) {
            Log.e(tag, "Error searching charging stations: ${e.message}", e)
            // Re-throw or return specific error state if needed, but for now log and empty
            emptyList()
        }
    }

    suspend fun searchAlongRoute(
        path: List<LatLng>,
        radiusMeters: Double
    ): List<ChargingSpot> {
        if (path.isEmpty()) return emptyList()

        val uniqueSpots = mutableMapOf<String, ChargingSpot>()
        
        // Search at start, middle, and end, and some points in between if long enough
        val searchPoints = mutableListOf<LatLng>()
        searchPoints.add(path.first())
        if (path.size > 2) {
            searchPoints.add(path[path.size / 2])
        }
        searchPoints.add(path.last())
        
        // If route is long, add more points
        // For simplicity, just use these 3 for now, or we could calculate distance
        
        searchPoints.forEach { point ->
            val spots = searchChargingStations(point, radiusMeters)
            spots.forEach { spot ->
                uniqueSpots[spot.id] = spot
            }
        }
        
        return uniqueSpots.values.toList()
    }
}
