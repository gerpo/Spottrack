package com.gerpo.spottrack.database.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlists", indices = [Index(value = ["id"], unique = true)])
data class Playlist(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var title: String = "",
        var spotify_id: String = "",
        var spotify_uri: String = "",
        var type: Int = 1,
        @Nullable var owner_name: String? = null,
        @Nullable var owner_id: String? = null,
        var completed: Boolean = false,
        @Nullable var image_url: String? = null) {
    fun isSaved(): Boolean {
        return (id > 0)
    }

    companion object {
        const val TYPE_ALBUM = 1
        const val TYPE_PLAYLIST = 2
    }
}