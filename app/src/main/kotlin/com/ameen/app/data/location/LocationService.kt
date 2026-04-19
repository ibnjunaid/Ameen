package com.ameen.app.data.location

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.ameen.app.domain.model.LocationData
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private val _locationFlow = MutableSharedFlow<LocationData>()
    val locationFlow = _locationFlow.asSharedFlow()

    private var isL5Active = false

    private val gnssStatusCallback = object : GnssStatus.Callback() {
        override fun onSatelliteStatusChanged(status: GnssStatus) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

            var l5Found = false
            for (i in 0 until status.satelliteCount) {
                // L5 is approx 1176.45 MHz
                val freq = status.getCarrierFrequencyHz(i)
                if (freq > 1.1e9f && freq < 1.2e9f) {
                    l5Found = true
                    break
                }
            }
            isL5Active = l5Found
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.lastLocation?.let { location ->
                serviceScope.launch {
                    _locationFlow.emit(location.toLocationData(isL5Active))
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        startLocationUpdates()
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(500)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        try {
            locationManager.registerGnssStatusCallback(gnssStatusCallback, null)
        } catch (e: SecurityException) {
            // Log or handle
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "location")
            .setContentTitle("Ameen Surveying")
            .setContentText("Tracking location in background")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
    }

    private fun Location.toLocationData(isL5: Boolean): LocationData {
        return LocationData(
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            accuracy = accuracy,
            speed = speed,
            bearing = bearing,
            timestamp = time,
            isL5 = isL5
        )
    }
}
