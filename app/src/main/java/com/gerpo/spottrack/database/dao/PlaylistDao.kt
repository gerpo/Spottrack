package com.gerpo.spottrack.database.dao

import androidx.room.*
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.database.entities.Track


@Dao
abstract class PlaylistDao {
    @Insert
    abstract fun insert(playlist: Playlist): Long

    @Transaction
    @Query("SELECT * FROM playlists")
    abstract fun getAllPlaylists(): List<PlaylistWithTracks>

    @Transaction
    @Query("SELECT * FROM playlists WHERE spotify_id = :spotifyId")
    abstract fun findBySpotifyId(spotifyId: String): Playlist?

    @Transaction
    @Query("SELECT * FROM playlists WHERE spotify_id = :spotifyId")
    abstract fun findBySpotifyIdWithTracks(spotifyId: String): PlaylistWithTracks?

    @Transaction
    @Insert
    fun insertWithTracks(playlist: Playlist, tracks: List<Track>): Long {
        val pId = if (playlist.isSaved()) playlist.id else insert(playlist)
        tracks.forEach { it.playlist_id = pId }
        insertTracks(tracks)

        return pId
    }

    @Transaction
    @Insert
    fun insertWithTracks(playlistWithTracks: PlaylistWithTracks): Long {
        val playlist = playlistWithTracks.playlist
        val tracks = playlistWithTracks.tracks
        val pId = if (playlist.isSaved()) playlist.id else insert(playlist)
        if (tracks != null) {
            tracks.forEach { it.playlist_id = pId }
            insertTracks(tracks)
        }
        return pId
    }

    @Insert
    abstract fun insertTracks(tracks: List<Track>)

    @Delete
    abstract fun deletePlaylist(playlist: Playlist)
}