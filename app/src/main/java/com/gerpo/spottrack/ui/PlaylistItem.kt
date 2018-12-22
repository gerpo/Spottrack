package com.gerpo.spottrack.ui

import com.gerpo.spottrack.SwitchListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kotlinx.android.synthetic.main.list_item_view.*

class PlaylistItem(private val item: PlaylistSimple, switchListener: SwitchListener, isTracked: Boolean) : SearchResultItem(switchListener, isTracked) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

        viewHolder.title.text = item.name
        viewHolder.desc.text = item.owner.display_name

        Picasso.get().load(item.images.last().url).into(viewHolder.cover)
    }
}