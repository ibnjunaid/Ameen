package com.ameen.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val startTime: Long = System.currentTimeMillis(),
    val startLat: Double? = null,
    val startLon: Double? = null,
    val endLat: Double? = null,
    val endLon: Double? = null
)

@Entity(tableName = "path_points")
data class PathPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val isCorrected: Boolean
)
