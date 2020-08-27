package com.thesurix.gesturerecycler

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.*
import com.thesurix.gesturerecycler.LayoutFlags.*

/**
 * Touch helper callback that handles different RecycleView gestures.
 * Constructs callback object based on passed adapter.
 * @param adapter adapter
 * @author thesurix
 */
private val DIRECTIONS = listOf(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP, ItemTouchHelper.DOWN)
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
        val holder = viewHolder as GestureViewHolder<*>
        return makeMovementFlags(if (holder.canDrag()) dragFlags else 0, if (holder.canSwipe()) swipeFlags else 0)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return gestureAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        gestureAdapter.onItemDismissed(viewHolder.adapterPosition, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is GestureViewHolder<*>) {
            viewHolder.onItemSelect()
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        when(actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                val direction = when {
                    dX.compareTo(0f) == 0 -> if (dY < 0) ItemTouchHelper.UP else ItemTouchHelper.DOWN
                    dY.compareTo(0f) == 0 -> if (dX < 0) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
                    else -> -1
                }

                val gestureViewHolder = (viewHolder as GestureViewHolder<*>)
                hideBackgroundViews(gestureViewHolder)

                if (direction != -1) {
                    val backgroundView = gestureViewHolder.getBackgroundView(direction)
                    backgroundView?.let {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && backgroundView.visibility == View.GONE) {
                            backgroundView.visibility = View.VISIBLE
                        }
                    }
                }

                val foregroundView = gestureViewHolder.foregroundView
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }
            else -> super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        gestureAdapter.onItemMoved()
        if (viewHolder is GestureViewHolder<*>) {
            viewHolder.onItemClear()
            hideBackgroundViews(viewHolder)

            val foregroundView = viewHolder.foregroundView
            getDefaultUIUtil().clearView(foregroundView)
        }
    }

    private fun hideBackgroundViews(viewHolder: GestureViewHolder<*>) {
        DIRECTIONS.forEach {
            viewHolder.getBackgroundView(it)?.visibility = View.GONE
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
