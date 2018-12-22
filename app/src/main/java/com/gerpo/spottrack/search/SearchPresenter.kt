package com.gerpo.spottrack.search

import android.content.Context
import com.gerpo.spottrack.BasePresenter
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.database.entities.AlbumWithTracks
import com.gerpo.spottrack.database.entities.ItemWithTracks
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.spotify.SearchPager
import com.gerpo.spottrack.spotify.SpotifyCallback
import com.gerpo.spottrack.spotify.SpotifyService
import com.gerpo.spottrack.transformer.PlaylistTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit.client.Response
import java.util.*
import javax.inject.Inject

class SearchPresenter @Inject constructor(val view: SearchContract.SearchView, val context: Context) : BasePresenter(), SearchContract.SearchPresenter {

    @Inject
    lateinit var spotify: SpotifyService

    @Inject
    lateinit var playlistDataSource: PlaylistDataSource

    @Inject
    lateinit var playlistTransformer: PlaylistTransformer

    private lateinit var currentPlaylists: List<PlaylistWithTracks>
    private lateinit var currentAlbums: List<PlaylistWithTracks>


    override fun search(query: String, offset: Int) {

        val options = HashMap<String, Any>()
        options[SpotifyService.OFFSET] = offset
        options[SpotifyService.LIMIT] = 50

        view.showProgress(true)

        spotify.searchAll(query, options, object : SpotifyCallback<SearchPager>(context) {
            override fun success(resultData: SearchPager, response: Response) {
                launch {
                    withContext(Dispatchers.IO) {
                        currentPlaylists = playlistTransformer.fromPlaylistSimpleWithTracks(resultData.playlists.items).toList()
                        currentAlbums = playlistTransformer.fromAlbumSimpleWithTracks(resultData.albums.items).toList()
                    }
                    view.showResults(resultData.playlists, resultData.albums)
                    view.showProgress(false)
                }
            }
        })
    }

    override fun savePlaylist(spotifyId: String) {
        val entity = getPlaylistWithId(spotifyId)
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

    override fun deletePlaylist(spotifyId: String) {
        val entity = getPlaylistWithId(spotifyId)
        if (entity.playlist.isSaved()) {
            launch {
                withContext(Dispatchers.IO) {
                    playlistDataSource.deletePlaylist(entity.playlist)
                }
            }
        }
    }

    override fun getPlaylistWithId(spotifyId: String): PlaylistWithTracks {
         return currentAlbums.union(currentPlaylists).first{it.playlist.spotify_id == spotifyId}
    }
}