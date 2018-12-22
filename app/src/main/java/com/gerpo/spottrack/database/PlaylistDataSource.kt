package com.gerpo.spottrack.database

import android.util.Log
import com.gerpo.spottrack.spotify.SpotifyService
import com.gerpo.spottrack.database.dao.PlaylistDao
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.database.entities.Track
import com.gerpo.spottrack.transformer.TrackTransformer
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit.RetrofitError
import java.util.HashMap

class PlaylistDataSource(val dao: PlaylistDao, val spotify: SpotifyService) {

    suspend fun getAllPlaylists(): List<PlaylistWithTracks> {
        return GlobalScope.async(Dispatchers.IO) { dao.getAllPlaylists() }.await()
    }

    suspend fun insert(playlist: Playlist): Long {
        return GlobalScope.async(Dispatchers.IO) { dao.insert(playlist) }.await()
    }

    suspend fun insertWithTracks(playlist: PlaylistWithTracks): Long {
        return GlobalScope.async(Dispatchers.IO) { dao.insertWithTracks(playlist) }.await()
    }

    suspend fun insertWithTracks(playlist: Playlist, tracks: List<Track>): Long {
        return GlobalScope.async(Dispatchers.IO) { dao.insertWithTracks(playlist, tracks) }.await()
    }

    suspend fun findBySpotifyId(spotifyId: String): Playlist? {
        return GlobalScope.async(Dispatchers.IO) { dao.findBySpotifyId(spotifyId) }.await()
    }

    suspend fun findBySpotifyIdWithTracks(spotifyId: String): PlaylistWithTracks? {
        return GlobalScope.async(Dispatchers.IO) { dao.findBySpotifyIdWithTracks(spotifyId) }.await()
    }

    suspend fun firstOrNew(spotifyId: String): Playlist {
        return GlobalScope.async(Dispatchers.IO) { dao.findBySpotifyId(spotifyId) ?: Playlist() }.await()
    }
    suspend fun deletePlaylist(playlist: Playlist) {
        GlobalScope.async(Dispatchers.IO) { dao.deletePlaylist(playlist) }.await()
        playlist.id = 0
    }

    suspend fun getTracksForPlaylist(entity: Playlist, offset: Int = 0): MutableList<Track> {
        val tracks: MutableList<Track> = mutableListOf()

        val options = HashMap<String, Any>()
        options[SpotifyService.OFFSET] = offset
        options[SpotifyService.LIMIT] = 50

        try {
            var result : Iterable<kaaes.spotify.webapi.android.models.Track> = listOf()
            var pager : Pager<*>? = null
            when(entity.type){
                Playlist.TYPE_ALBUM -> {
                    pager = spotify.getAlbumTracks(entity.spotify_id, options)
                    result = pager.items
                }
                Playlist.TYPE_PLAYLIST -> {
                    pager = spotify.getPlaylistTracks(entity.spotify_id, options)
                    result = pager.items.map { it.track }
                }
            }

            tracks.addAll(TrackTransformer.fromTrack(result))

            if (pager?.next != null) tracks.addAll(getTracksForPlaylist(entity, offset + 50))


        } catch (e: RetrofitError) {
            Log.i("TAG", e.toString())
        }

        return tracks
    }
}