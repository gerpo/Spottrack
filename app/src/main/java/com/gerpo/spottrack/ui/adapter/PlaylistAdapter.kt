package com.gerpo.spottrack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gerpo.spottrack.R
import com.gerpo.spottrack.SwitchListener
import com.gerpo.spottrack.database.entities.Playlist
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_view.view.*

class PlaylistAdapter(val data: List<Playlist>, private val switchListener: SwitchListener) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_view, parent, false) as ConstraintLayout
        return ViewHolder(v, switchListener)
    }

    class ViewHolder(val view: ConstraintLayout, private val switchListener: SwitchListener) : RecyclerView.ViewHolder(view) {
        fun bindItem(data: Playlist) {
            view.title.text = data.title
            view.desc.text = data.owner_name

            if (data.image_url != null)
                Picasso.get().load(data.image_url).into(view.cover)
            else
                view.cover.setImageURI(null)

            view.addSwitch.isChecked = data.isSaved()

            view.addSwitch.setOnClickListener { switchListener.onSwitchPressed(adapterPosition, (it as Switch).isChecked) }
            view.list_item.setOnClickListener { switchListener.onItemPressed(adapterPosition) }
        }
    }
}