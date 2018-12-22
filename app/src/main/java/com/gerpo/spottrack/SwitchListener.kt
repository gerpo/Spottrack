package com.gerpo.spottrack

interface SwitchListener {
    fun onSwitchPressed(position: Int, isChecked: Boolean)
    fun onItemPressed(adapterPosition: Int)
}