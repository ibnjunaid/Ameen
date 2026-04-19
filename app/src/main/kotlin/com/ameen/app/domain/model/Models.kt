package com.ameen.app.domain.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val accuracy: Float = 0f,
    val speed: Float = 0f,
    val bearing: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val isL5: Boolean = false
)

data class DriftVector(
    val latDrift: Double,
    val lonDrift: Double,
    val timestamp: Long = System.currentTimeMillis()
)

enum class OperationMode {
    STANDALONE,
    BASE_STATION,
    ROVER
}
