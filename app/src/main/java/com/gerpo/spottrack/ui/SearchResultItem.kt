package com.gerpo.spottrack.ui

import android.widget.Switch
import com.gerpo.spottrack.R
import com.gerpo.spottrack.SwitchListener
import com.gerpo.spottrack.database.PlaylistDataSource
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.list_item_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class SearchResultItem(private val switchListener: SwitchListener, private val isTracked: Boolean) : Item() {
    override fun getLayout() = R.layout.list_item_view

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.addSwitch.isChecked = isTracked

        viewHolder.addSwitch.setOnClickListener { switchListener.onSwitchPressed(position, (it as Switch).isChecked) }
        viewHolder.list_item.setOnClickListener { switchListener.onItemPressed(position) }
    }
}