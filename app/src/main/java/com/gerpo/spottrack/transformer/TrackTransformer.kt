package com.gerpo.spottrack.transformer

import com.gerpo.spottrack.database.entities.PlayedTrack
import com.gerpo.spottrack.database.entities.Track
import kaaes.spotify.webapi.android.models.PlaylistTrack

class TrackTransformer {
    companion object {

        fun fromTrack(source: kaaes.spotify.webapi.android.models.Track): Track {
            return Track().apply {
                title = source.name
                duration = source.duration_ms
                spotify_id = source.id
                spotify_uri = source.uri
            }
        }

        fun fromPlaylistTrack(source: PlaylistTrack): Track {
            return Track().apply {
                title = source.track.name
                duration = source.track.duration_ms
                spotify_id = source.track.id
                spotify_uri = source.track.uri
            }
        }

        fun fromTrack(source: Iterable<kaaes.spotify.webapi.android.models.Track>): Iterable<Track> {
            return source.map { fromTrack(it) }
        }

        fun fromPlaylistTrack(source: Iterable<PlaylistTrack>): Iterable<Track> {
            return source.filter { it.track.id != null }.map { fromPlaylistTrack(it) }
        }

        fun fromPlayedTrack(source: Iterable<PlayedTrack>): Iterable<Track> {
            return source.map { fromTrack(it.track) }
        }
    }
}