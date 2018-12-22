package com.gerpo.spottrack.overview


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gerpo.spottrack.BaseFragment
import com.gerpo.spottrack.ui.dialogs.ConfirmDialog
import com.gerpo.spottrack.R
import com.gerpo.spottrack.SwitchListener
import com.gerpo.spottrack.ui.adapter.PlaylistAdapter
import com.gerpo.spottrack.database.PlaylistDataSource
import com.gerpo.spottrack.detail.DetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject


class OverviewFragment :  BaseFragment(), OverviewContract.OverviewView {


    @Inject
    lateinit var playlistDataSource: PlaylistDataSource

    @Inject
    lateinit var presenter: OverviewPresenter

    //var aboItems: MutableList<Playlist> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        presenter.playlistDataSource
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onStart() {
        super.onStart()

        aboView.layoutManager = LinearLayoutManager(context)

        swiperefresh.setOnRefreshListener {
            presenter.syncTracks()
        }
    }

    override fun showResults() {
        val items = presenter.trackedPlaylists.map { it.playlist }
        aboView.adapter = PlaylistAdapter(items, object : SwitchListener {
            override fun onItemPressed(adapterPosition: Int) {
                val pl = Gson().toJson(presenter.trackedPlaylists[adapterPosition])
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("playlist", pl)

                startActivity(intent)
            }

            override fun onSwitchPressed(position: Int, isChecked: Boolean) {
                if (isChecked) {
                    presenter.savePlaylist(position)
                } else {
                    context?.let {
                        ConfirmDialog.getCreateDialog(it)
                                .setPositiveButton(R.string.confirm)
                                { _, _ ->  presenter.deletePlaylist(position) }
                                .show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        presenter.syncTracks()
    }

    override fun showProgress(show: Boolean) {
        swiperefresh.isRefreshing = show
    }
}
