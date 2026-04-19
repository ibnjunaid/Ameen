package com.ameen.app.data.firebase

import com.ameen.app.domain.model.DriftVector
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseSyncManager @Inject constructor(
    private val db: FirebaseDatabase
) {
    private val sessionsRef = db.getReference("sessions")

    fun publishDrift(sessionId: String, drift: DriftVector) {
        sessionsRef.child(sessionId).child("drift").setValue(drift)
    }

    fun observeDrift(sessionId: String): Flow<DriftVector> = callbackFlow {
        val driftRef = sessionsRef.child(sessionId).child("drift")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("latDrift").getValue(Double::class.java) ?: 0.0
                val lon = snapshot.child("lonDrift").getValue(Double::class.java) ?: 0.0
                val ts = snapshot.child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()
                trySend(DriftVector(lat, lon, ts))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        driftRef.addValueEventListener(listener)
        awaitClose { driftRef.removeEventListener(listener) }
    }

    fun createSession(sessionId: String, hostName: String) {
        sessionsRef.child(sessionId).child("host").setValue(hostName)
    }
}
