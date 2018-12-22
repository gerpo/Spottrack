package com.gerpo.spottrack.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gerpo.spottrack.database.entities.Track

@Dao
abstract class TrackDao {
    @Insert
    abstract fun insertTracks(tracks: List<Track>)

    @Query("SELECT * FROM tracks WHERE playlist_id = :playlistId")
    abstract fun findByPlaylistId(playlistId: Long): List<Track>?

    @Query("SELECT * FROM tracks")
    abstract fun getAll(): List<Track>?

    @Query("SELECT * FROM tracks WHERE completed = :bool")
    abstract fun getAllIncomplete(bool: Boolean = false): List<Track>?

    @Query("UPDATE tracks SET completed = 1 WHERE id in (:ids)")
    abstract fun markAsComplete(ids: List<Int>)
}