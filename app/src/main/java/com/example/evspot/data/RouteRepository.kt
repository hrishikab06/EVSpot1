package com.example.evspot.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteRepository(private val apiKey: String) {
    private val geoApiContext by lazy {
        GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()
    }

    suspend fun getRoute(origin: LatLng, destination: LatLng): DirectionsResult? = withContext(Dispatchers.IO) {
        try {
            DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
