package com.gerpo.spottrack.detail

import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.database.entities.Track

interface DetailContract {
    interface DetailView {
        fun insertPlaylistData(playlist: Playlist)
        fun insertTracks(tracks: List<Track>)
        fun showProgress(show: Boolean = true)
    }

    interface DetailPresenter {
        var playlist: Playlist
        fun getPlaylistData(providedPlaylist: PlaylistWithTracks)
        fun displayPlaylist()
        fun displayTracks()
        fun savePlaylist()
        fun deletePlaylist()
    }
}