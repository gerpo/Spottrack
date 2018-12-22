package com.gerpo.spottrack.transformer

import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.TrackDataSource
import com.gerpo.spottrack.database.entities.AlbumWithTracks
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import kaaes.spotify.webapi.android.models.AlbumSimple
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kaaes.spotify.webapi.android.models.PlaylistTrack
import javax.inject.Inject

class PlaylistTransformer @Inject constructor(private val pDao: PlaylistDataSource, private val tDao: TrackDataSource) {

    suspend fun fromPlaylistSimple(source: PlaylistSimple): Playlist {

        val result = pDao.firstOrNew(source.id)

        if (!result.isSaved()) {
            result.apply {
                title = source.name
                spotify_id = source.id
                spotify_uri = source.uri
                owner_id = source.owner.id
                owner_name = source.owner.display_name
                image_url = source.images.last().url
                type = Playlist.TYPE_PLAYLIST
            }
        }

        return result
    }

    suspend fun fromAlbumSimple(source: AlbumSimple): Playlist {

        val result = pDao.firstOrNew(source.id)

        if (!result.isSaved()) {
            result.apply {
                title = source.name
                spotify_id = source.id
                spotify_uri = source.uri
                image_url = source.images.last().url
                type = Playlist.TYPE_ALBUM
            }
        }

        return result
    }

    suspend fun fromPlaylistSimple(source: Iterable<PlaylistSimple>): Iterable<Playlist> {
        return source.map { fromPlaylistSimple(it) }
    }

    suspend fun fromPlaylistSimpleWithTracks(source: PlaylistSimple): PlaylistWithTracks {
        val entity = PlaylistWithTracks(fromPlaylistSimple(source))
        entity.tracks = tDao.findByPlaylistId(entity.playlist.id)
        return entity
    }

    suspend fun fromPlaylistSimpleWithTracks(source: Iterable<PlaylistSimple>): Iterable<PlaylistWithTracks> {
        return source.map { fromPlaylistSimpleWithTracks(it) }
    }

    suspend fun fromPlaylistSimpleWithTracks(source: PlaylistSimple, trackSource: Iterable<PlaylistTrack>): PlaylistWithTracks {
        val result = PlaylistWithTracks(fromPlaylistSimple(source))
        result.tracks = TrackTransformer.fromPlaylistTrack(trackSource).toList()

        return result
    }

    suspend fun fromAlbumSimpleWithTracks(source: AlbumSimple): PlaylistWithTracks {
        val entity = PlaylistWithTracks(fromAlbumSimple(source))
        entity.tracks = tDao.findByPlaylistId(entity.playlist.id)
        return entity
    }

    suspend fun fromAlbumSimpleWithTracks(source: Iterable<AlbumSimple>): Iterable<PlaylistWithTracks> {
        return source.map { fromAlbumSimpleWithTracks(it) }
    }
}