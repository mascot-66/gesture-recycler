package com.thesurix.gesturerecycler

import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.thesurix.gesturerecycler.LayoutFlags.*

/**
 * Touch helper callback that handles different RecycleView gestures.
 * Constructs callback object based on passed adapter.
 * @param adapter adapter
 * @author thesurix
 */
class GestureTouchHelperCallback(private val gestureAdapter: GestureAdapter<*, *>) : ItemTouchHelper.Callback() {

    /** Flag that enables or disables swipe gesture  */
    var swipeEnabled = false
    /** Flag that enables or disables manual drag gesture  */
    var manualDragEnabled = false
        set(enabled) {
            field = enabled
            gestureAdapter.allowManualDrag(manualDragEnabled)
        }
    /** Flag that enables long press drag gesture  */
    var longPressDragEnabled = false

    /** Flags for drag gesture  */
    var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    /** Flags for swipe gesture  */
    var swipeFlags = ItemTouchHelper.RIGHT

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val holder = viewHolder as GestureViewHolder
        return ItemTouchHelper.Callback.makeMovementFlags(if (holder.canDrag()) dragFlags else 0, if (holder.canSwipe()) swipeFlags else 0)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return gestureAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        gestureAdapter.onItemDismissed(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is GestureViewHolder) {
            val backgroundView = viewHolder.backgroundView
            backgroundView?.let {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    backgroundView.visibility = View.VISIBLE
                }
            }

            viewHolder.onItemSelect()
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        when(actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                val foregroundView = (viewHolder as GestureViewHolder).foregroundView
                ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }
            else -> super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        gestureAdapter.onItemMoved()
        if (viewHolder is GestureViewHolder) {
            viewHolder.onItemClear()

            val backgroundView = viewHolder.backgroundView
            backgroundView?.visibility = View.GONE

            val foregroundView = viewHolder.foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return longPressDragEnabled
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return swipeEnabled
    }

    /**
     * Sets predefined drag flags for RecyclerView layout.
     * @param layout type of the RecyclerView layout
     */
    fun setDragFlagsForLayout(layout: RecyclerView.LayoutManager) {
        dragFlags = when (layout) {
            is GridLayoutManager -> GRID.getDragFlags(layout)
            is LinearLayoutManager -> LINEAR.getDragFlags(layout)
            is StaggeredGridLayoutManager -> STAGGERED.getDragFlags(layout)
            else -> throw IllegalArgumentException("Unsupported layout type.")
        }
    }

    /**
     * Sets predefined swipe flags for RecyclerView layout.
     * @param layout type of the RecyclerView layout
     */
    fun setSwipeFlagsForLayout(layout: RecyclerView.LayoutManager) {
        swipeFlags = when (layout) {
            is GridLayoutManager -> GRID.getSwipeFlags(layout)
            is LinearLayoutManager -> LINEAR.getSwipeFlags(layout)
            is StaggeredGridLayoutManager -> STAGGERED.getSwipeFlags(layout)
            else -> throw IllegalArgumentException("Unsupported layout type.")
        }
    }
}
