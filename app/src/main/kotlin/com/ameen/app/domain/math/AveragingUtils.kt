package com.ameen.app.domain.math

import com.ameen.app.domain.model.LocationData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

object AveragingUtils {
    /**
     * Collects 30 samples at 1Hz and calculates the centroid.
     */
    suspend fun calculateCentroid(locationFlow: Flow<LocationData>): LocationData? {
        val samples = mutableListOf<LocationData>()
        // This is a simplified version. In a real app, we'd ensure 1Hz sampling.
        locationFlow.take(30).collect {
            samples.add(it)
        }

        if (samples.isEmpty()) return null

        val avgLat = samples.map { it.latitude }.average()
        val avgLon = samples.map { it.longitude }.average()
        val avgAlt = samples.map { it.altitude }.average()

        return samples.first().copy(
            latitude = avgLat,
            longitude = avgLon,
            altitude = avgAlt,
            timestamp = System.currentTimeMillis()
        )
    }
}
