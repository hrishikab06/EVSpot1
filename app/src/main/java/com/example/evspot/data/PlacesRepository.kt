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
import kotlinx.coroutines.tasks.await

class PlacesRepository(context: Context) {
    private val placesClient: PlacesClient = Places.createClient(context)
    private val TAG = "PlacesRepository"

    suspend fun searchChargingStations(
        center: LatLng,
        radiusMeters: Double
    ): List<ChargingSpot> {
        Log.d(TAG, "Searching charging stations around: $center with radius: $radiusMeters")
        
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
            Log.d(TAG, "Found ${spots.size} stations")
            spots.forEach { spot ->
                Log.d(TAG, "Station: ${spot.name} at ${spot.position}")
            }
            spots
        } catch (e: Exception) {
            Log.e(TAG, "Error searching charging stations: ${e.message}", e)
            // Re-throw or return specific error state if needed, but for now log and empty
            emptyList()
        }
    }
}
