package com.gerpo.spottrack.detail

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gerpo.spottrack.ui.dialogs.ConfirmDialog
import com.gerpo.spottrack.R
import com.gerpo.spottrack.ui.adapter.TrackAdapter
import com.gerpo.spottrack.database.entities.Playlist
import com.gerpo.spottrack.database.entities.PlaylistWithTracks
import com.gerpo.spottrack.database.entities.Track
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), DetailContract.DetailView {

    @Inject
    lateinit var presenter: DetailContract.DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        trackView.isFocusable = false

        trackView.layoutManager = LinearLayoutManager(this)

        addSwitch.setOnClickListener {
            (it as Switch).let {
                if (it.isChecked) {
                    presenter.savePlaylist()
                } else {
                    ConfirmDialog.getCreateDialog(this)
                            .setPositiveButton(R.string.confirm)
                            { _, _ -> presenter.deletePlaylist() }
                            .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        when {
            intent.hasExtra("playlist") -> presenter.getPlaylistData(Gson().fromJson(intent.getStringExtra("playlist"), PlaylistWithTracks::class.java))
            else
            -> {
                Toast.makeText(this, "Playlist cannot be shown.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        addSwitch.isChecked = presenter.playlist.isSaved()
    }

    override fun insertPlaylistData(playlist: Playlist) {
        playlist_title.text = playlist.title
        addSwitch.isChecked = playlist.isSaved()

        Picasso.get().load(playlist.image_url).into(cover)
    }

    override fun insertTracks(tracks: List<Track>) {
        trackView.adapter = TrackAdapter(tracks, this)
    }

    override fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) ProgressBar.VISIBLE else ProgressBar.GONE
    }
}
