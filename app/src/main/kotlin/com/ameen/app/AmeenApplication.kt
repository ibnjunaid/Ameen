package com.ameen.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.database.FirebaseDatabase

@HiltAndroidApp
class AmeenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
