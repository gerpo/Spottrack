package com.gerpo.spottrack.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.gerpo.spottrack.BaseFragment
import com.gerpo.spottrack.R
import com.gerpo.spottrack.SwitchListener
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.detail.DetailActivity
import com.gerpo.spottrack.ui.AlbumItem
import com.gerpo.spottrack.ui.ParentListItem
import com.gerpo.spottrack.ui.PlaylistItem
import com.gerpo.spottrack.ui.dialogs.ConfirmDialog
import com.google.gson.Gson
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kaaes.spotify.webapi.android.models.Album
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


class SearchFragment : BaseFragment(), SearchContract.SearchView {

    @Inject
    lateinit var presenter: SearchPresenter

    @Inject
    lateinit var playlistDataSource: PlaylistDataSource

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onStart() {
        super.onStart()

        searchField.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) presenter.search(query)

                searchField.clearFocus()
                return true
            }

        })
    }

    override fun showResults(playlists: Pager<PlaylistSimple>, albums: Pager<Album>) {

        val adapter = GroupAdapter<ViewHolder>()

        resultList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        ExpandableGroup(ParentListItem("Albums"), true).apply {
            add(Section(albums.items.map { AlbumItem(it, getSwitchListener(it.id), presenter.getPlaylistWithId(it.id).playlist.isSaved()) }))
            adapter.add(this)
        }

        ExpandableGroup(ParentListItem("Playlists"), false).apply {
            add(Section(playlists.items.map { PlaylistItem(it, getSwitchListener(it.id), presenter.getPlaylistWithId(it.id).playlist.isSaved()) }))
            adapter.add(this)
        }
    }

    override fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) ProgressBar.VISIBLE else ProgressBar.GONE
    }

    private fun getSwitchListener(spotifyId: String): SwitchListener {
        return object : SwitchListener {
            override fun onItemPressed(adapterPosition: Int) {
                val pl = Gson().toJson(presenter.getPlaylistWithId(spotifyId))
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("playlist", pl)

                startActivity(intent)
            }

            override fun onSwitchPressed(position: Int, isChecked: Boolean) {
                if (isChecked) {
                    presenter.savePlaylist(spotifyId)
                } else {
                    context?.let {
                        ConfirmDialog.getCreateDialog(it)
                                .setPositiveButton(R.string.confirm)
                                { _, _ -> presenter.deletePlaylist(spotifyId) }
                                .show()
                    }
                }
            }
        }

    }
}
