package com.ameen.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SessionEntity::class, PathPointEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun pathPointDao(): PathPointDao
}
