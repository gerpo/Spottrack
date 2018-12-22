package com.gerpo.spottrack.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.gerpo.spottrack.R
import com.gerpo.spottrack.database.entities.Track
import kotlinx.android.synthetic.main.track_item_view.view.*

class TrackAdapter(val data: List<Track>, val context: Context) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.track_item_view, parent, false) as ConstraintLayout
        return ViewHolder(v, context)
    }

    class ViewHolder(val view: ConstraintLayout, val context: Context) : RecyclerView.ViewHolder(view) {
        fun bindItem(data: Track) {
            view.title.text = data.title
            view.duration.text = data.durationString()

            view.detail_item.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.spotify_uri))
                startActivity(context, intent, null)
            }

            if (data.completed)
                view.title.setTextColor(ContextCompat.getColor(context, R.color.colorMuted))
        }
    }
}