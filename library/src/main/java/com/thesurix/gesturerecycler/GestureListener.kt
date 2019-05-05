package com.thesurix.gesturerecycler


import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Default gesture listener that handles manual spawned drag gestures.
 * @author thesurix
 */
class GestureListener(private val touchHelper: ItemTouchHelper) : GestureAdapter.OnGestureListener {

    override fun onStartDrag(viewHolder: GestureViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}
