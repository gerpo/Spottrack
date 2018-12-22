package com.gerpo.spottrack.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.concurrent.TimeUnit

@Entity(tableName = "tracks",
        foreignKeys = [ForeignKey(entity = Playlist::class, parentColumns = ["id"], childColumns = ["playlist_id"], onDelete = CASCADE)],
        indices = [Index(value = ["id", "playlist_id"])])
data class Track(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                 var title: String = "",
                 var duration: Long = 0,
                 var spotify_id: String = "",
                 var spotify_uri: String = "",
                 var playlist_id: Long = 0,
                 var completed: Boolean = false) {

    fun durationString(): String {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(this.duration),
                TimeUnit.MILLISECONDS.toSeconds(this.duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.duration))
        );
    }

    override fun equals(other: Any?): Boolean {
        if (other is Track) return spotify_uri == other.spotify_uri

        return super.equals(other)
    }
}