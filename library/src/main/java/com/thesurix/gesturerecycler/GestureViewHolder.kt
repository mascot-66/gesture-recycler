package com.thesurix.gesturerecycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Base view holder class for gesture compatible items.
 * @author thesurix
 */
abstract class GestureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Returns view that can spawn drag gesture. If there is no view simply return null.
     * @return view that can spawn drag gesture
     */
    open val draggableView: View?
        get() = null

    /**
     * Returns top visible view (originally root view of the item),
     * override this method to use background view feature in case of swipe gestures.
     * @return top view
     */
    open val foregroundView: View
        get() = itemView

    /**
     * Returns background view which is visible when foreground view is partially or fully swiped.
     * @return background view
     */
    open val backgroundView: View?
        get() = null

    /**
     * Method that shows view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    fun showDraggableView() {
        draggableView?.visibility = View.VISIBLE
    }

    /**
     * Method that hides view for manual drag gestures.
     * Called only when getDraggableView() returns valid view.
     */
    fun hideDraggableView() {
        draggableView?.visibility = View.GONE
    }

    /**
     * Indicates that view is selected.
     */
    open fun onItemSelect() {}

    /**
     * Indicates that view has no selection.
     */
    open fun onItemClear() {}

    /**
     * Returns information if we can drag this view.
     * @return true if draggable, false otherwise
     */
    abstract fun canDrag(): Boolean

    /**
     * Returns information if we can swipe this view.
     * @return true if swipeable, false otherwise
     */
    abstract fun canSwipe(): Boolean
}
