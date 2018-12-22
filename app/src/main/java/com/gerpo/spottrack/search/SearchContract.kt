package com.gerpo.spottrack.search

import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple

interface SearchContract {
    interface SearchView {
        fun showResults(pager: Pager<PlaylistSimple>, albums: Pager<Album>)
        fun showProgress(show: Boolean)
    }
    interface SearchPresenter {
        fun search(query: String, offset: Int = 0)
        fun savePlaylist(spotifyId: String)
        fun deletePlaylist(spotifyId: String)
        fun getPlaylistWithId(spotifyId: String): PlaylistWithTracks
    }
}