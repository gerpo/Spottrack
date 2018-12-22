package com.gerpo.spottrack.ui

import com.gerpo.spottrack.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.parent_item_view.*

class ParentListItem(val header: String) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.header.text = header
        viewHolder.header.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, getIconResId(), 0)

        viewHolder.header.setOnClickListener{
            expandableGroup.onToggleExpanded()
            viewHolder.header.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, getIconResId(), 0)
        }
    }

    override fun getLayout(): Int = R.layout.parent_item_view

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getIconResId(): Int {
        if (expandableGroup.isExpanded) {
            return R.drawable.ic_keyboard_arrow_up_black_24dp
        }

        return R.drawable.ic_keyboard_arrow_down_black_24dp
    }
}