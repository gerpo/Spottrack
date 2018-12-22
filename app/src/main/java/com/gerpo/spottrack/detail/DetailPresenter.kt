package com.gerpo.spottrack.detail

import com.gerpo.spottrack.BasePresenter
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.database.entities.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailPresenter @Inject constructor(val view: DetailContract.DetailView, var playlistDataSource: PlaylistDataSource) : BasePresenter(), DetailContract.DetailPresenter {

    //@Inject
    //lateinit var playlistDataSource: PlaylistDataSource

    override lateinit var playlist: Playlist

    lateinit var tracks: List<Track>

    override fun getPlaylistData(providedPlaylist: PlaylistWithTracks) {
        playlist = providedPlaylist.playlist
        view.showProgress()

        if (!providedPlaylist.tracks.isNullOrEmpty()) {
            tracks = providedPlaylist.tracks!!

            view.insertTracks(tracks)
        } else {
            launch {

                tracks = async(Dispatchers.IO) {
                    playlistDataSource.getTracksForPlaylist(playlist)
                }.await()

                view.insertTracks(tracks)
            }
        }

        displayPlaylist()
    }

    override fun displayPlaylist() {
        view.insertPlaylistData(playlist)
    }

    override fun displayTracks() {
        view.insertTracks(tracks)
        view.showProgress(false)
    }

    override fun savePlaylist() {
        if (!playlist.isSaved()) {
            val entity = PlaylistWithTracks(playlist)
            entity.tracks = tracks
            launch {
                withContext(Dispatchers.IO) {
                    val pId = playlistDataSource.insertWithTracks(entity)
                    entity.playlist.id = pId
                }
            }
        }
    }

    override fun deletePlaylist() {
        val entity = playlist
        if (entity.isSaved()) {
            launch {
                withContext(Dispatchers.IO) {
                    playlistDataSource.deletePlaylist(entity)
                }
            }
        }
    }
}