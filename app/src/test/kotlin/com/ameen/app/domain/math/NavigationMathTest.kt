package com.ameen.app.domain.math

import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationMathTest {

    @Test
    fun testHaversineDistance() {
        // Distance between two points (approx 111km for 1 degree lat)
        val lat1 = 0.0
        val lon1 = 0.0
        val lat2 = 1.0
        val lon2 = 0.0
        val distance = NavigationMath.calculateHaversineDistance(lat1, lon1, lat2, lon2)
        assertEquals(111194.9, distance, 100.0)
    }

    @Test
    fun testBearing() {
        val lat1 = 0.0
        val lon1 = 0.0
        val lat2 = 0.0
        val lon2 = 1.0
        val bearing = NavigationMath.calculateBearing(lat1, lon1, lat2, lon2)
        assertEquals(90.0, bearing, 0.1)
    }
}
