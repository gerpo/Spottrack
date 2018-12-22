package com.gerpo.spottrack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gerpo.spottrack.R
import com.gerpo.spottrack.SwitchListener
import com.gerpo.spottrack.database.PlaylistDataSource
import com.squareup.picasso.Picasso
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kotlinx.android.synthetic.main.list_item_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SpotifyPlaylistAdapter(val data: MutableList<PlaylistSimple>, private val playlistDataSource: PlaylistDataSource, private val switchListener: SwitchListener) : RecyclerView.Adapter<SpotifyPlaylistAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_view, parent, false) as ConstraintLayout

        return ViewHolder(v, playlistDataSource, switchListener)
    }

    class ViewHolder(val view: ConstraintLayout, private val playlistDataSource: PlaylistDataSource, private val switchListener: SwitchListener) : RecyclerView.ViewHolder(view) {
        fun bindItem(data: PlaylistSimple) {
            view.title.text = data.name
            view.desc.text = data.owner.display_name

            Picasso.get().load(data.images.last().url).into(view.cover)

            GlobalScope.launch(Dispatchers.Main) {
                view.addSwitch.isChecked = isTracked(data.id)
            }

            view.addSwitch.setOnClickListener { switchListener.onSwitchPressed(adapterPosition, (it as Switch).isChecked) }
            view.list_item.setOnClickListener { switchListener.onItemPressed(adapterPosition) }
        }

        private suspend fun isTracked(spotifyId: String): Boolean {
            return (playlistDataSource.findBySpotifyId(spotifyId) != null)
        }
    }
}