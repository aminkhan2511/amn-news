package com.example.amnnews.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.ui.home.BreakingNewsAdapterHome

class SwipeToDeleteCallback(private val adapter: BreakingNewsAdapterHome) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // We are not handling move actions
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Handle the swipe action here
        val position = viewHolder.adapterPosition
        adapter.removeItem(position)
    }
}
