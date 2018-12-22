package com.gerpo.spottrack.overview

import android.content.SharedPreferences
import com.gerpo.spottrack.BasePresenter
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.spotify.SpotifySync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OverviewPresenter @Inject constructor(val view: OverviewContract.OverviewView, val prefs: SharedPreferences) : BasePresenter(), OverviewContract.OverviewPresenter {

    @Inject
    override lateinit var playlistDataSource: PlaylistDataSource

    @Inject
    lateinit var spotifySync: SpotifySync

    override lateinit var trackedPlaylists: List<PlaylistWithTracks>

    override fun syncTracks() {
        view.showProgress(true)
        launch {
            withContext(Dispatchers.IO) {
                spotifySync.checkRecentTracks()
            }

            trackedPlaylists = async(Dispatchers.IO) {
                playlistDataSource.getAllPlaylists()
            }.await()

            view.showResults()
            view.showProgress(false)
        }
    }

    override fun savePlaylist(position: Int) {
        val entity = trackedPlaylists[position]
        if (!entity.playlist.isSaved() || entity.tracks!!.isEmpty()) {
            launch {
                withContext(Dispatchers.IO) {
                    entity.tracks = playlistDataSource.getTracksForPlaylist(entity.playlist)
                    val pId = playlistDataSource.insertWithTracks(entity)
                    entity.playlist.id = pId
                }
            }
        }
    }

    override fun deletePlaylist(position: Int) {
        val entity = trackedPlaylists[position]
        if (entity.playlist.isSaved()) {
            launch {
                withContext(Dispatchers.IO) {
                    playlistDataSource.deletePlaylist(entity.playlist)
                }
            }
        }
    }
}