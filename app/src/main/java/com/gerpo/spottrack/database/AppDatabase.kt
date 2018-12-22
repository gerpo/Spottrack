package com.gerpo.spottrack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gerpo.spottrack.database.dao.PlaylistDao
import com.gerpo.spottrack.database.dao.TrackDao
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.Track

@Database(
        version = 1,
        entities = [
            Playlist::class,
            Track::class
        ]
)
abstract class AppDatabase : RoomDatabase() {


    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
                    .build()
        }
    }
}