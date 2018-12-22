package com.gerpo.spottrack.overview

import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.entities.PlaylistWithTracks

interface OverviewContract {
    interface OverviewView {
        fun showResults()
        fun showProgress(show: Boolean)
    }
    interface OverviewPresenter {
        var playlistDataSource: PlaylistDataSource
        var trackedPlaylists: List<PlaylistWithTracks>
        fun syncTracks()
        fun savePlaylist(position: Int)
        fun deletePlaylist(position: Int)
    }
}