package com.gerpo.spottrack.database.entities

import androidx.room.Embedded
import androidx.room.Relation

abstract class ItemWithTracks(@Embedded var playlist: Playlist) {

    @Relation(parentColumn = "id", entityColumn = "playlist_id", entity = Track::class)
    var tracks: List<Track>? = null
}