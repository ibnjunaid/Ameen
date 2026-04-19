package com.ameen.app.data.location

import com.ameen.app.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface MeasurementStrategy {
    fun processLocation(rawLocation: LocationData): LocationData
}

class StandaloneStrategy : MeasurementStrategy {
    override fun processLocation(rawLocation: LocationData): LocationData {
        // In standalone, we might apply local smoothing if needed,
        // but for now return as is.
        return rawLocation
    }
}

class DifferentialStrategy(
    private val driftFlow: Flow<com.ameen.app.domain.model.DriftVector>
) : MeasurementStrategy {
    private var lastDrift: com.ameen.app.domain.model.DriftVector? = null

    // This would be hooked up to collect from driftFlow in a real implementation
    fun updateDrift(drift: com.ameen.app.domain.model.DriftVector) {
        lastDrift = drift
    }

    override fun processLocation(rawLocation: LocationData): LocationData {
        val drift = lastDrift ?: return rawLocation
        return rawLocation.copy(
            latitude = rawLocation.latitude - drift.latDrift,
            longitude = rawLocation.longitude - drift.lonDrift
        )
    }
}
