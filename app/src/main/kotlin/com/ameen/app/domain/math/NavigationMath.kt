package com.ameen.app.domain.math

import kotlin.math.*

object NavigationMath {
    private const val EARTH_RADIUS = 6371000.0 // Meters

    /**
     * Calculates the initial bearing between two coordinates in degrees.
     */
    fun calculateBearing(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Double {
        val phi1 = Math.toRadians(startLat)
        val phi2 = Math.toRadians(endLat)
        val deltaLambda = Math.toRadians(endLon - startLon)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)

        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

    /**
     * Calculates the Haversine distance between two coordinates in meters.
     */
    fun calculateHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    /**
     * Calculates the cross-track error (XTE) in meters.
     * The perpendicular distance from current position to the line segment AB.
     */
    fun calculateCrossTrackError(
        currentLat: Double, currentLon: Double,
        startLat: Double, startLon: Double,
        endLat: Double, endLon: Double
    ): Double {
        val d13 = calculateHaversineDistance(startLat, startLon, currentLat, currentLon)
        val brng13 = Math.toRadians(calculateBearing(startLat, startLon, currentLat, currentLon))
        val brng12 = Math.toRadians(calculateBearing(startLat, startLon, endLat, endLon))

        return asin(sin(d13 / EARTH_RADIUS) * sin(brng13 - brng12)) * EARTH_RADIUS
    }
}
