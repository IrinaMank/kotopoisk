package com.zapir.kotopoisk.ui.tickets_recycler

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeCallback(private val swipedListener: (Int) -> Unit) : ItemTouchHelper.Callback() {

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipedListener(viewHolder.adapterPosition)
    }

}
