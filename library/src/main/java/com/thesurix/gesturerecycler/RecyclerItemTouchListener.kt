package com.thesurix.gesturerecycler

import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Class that is responsible for handling item touch events.
 * Constructs [RecyclerView] touch listener.
 * @param listener listener for item's click events
 * @author thesurix
 */
class RecyclerItemTouchListener<T>(listener: ItemClickListener<T>) : RecyclerView.SimpleOnItemTouchListener() {

    private var gestureDetector: GestureDetector? = null

    /**
     * The listener that is used to notify when a tap, long press or double tap occur.
     */
    interface ItemClickListener<T> {

        /**
         * Called when a tap occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onItemClick(item: T, position: Int): Boolean

        /**
         * Called when a long press occurs on a specified item.
         * @param item pressed item
         * @param position item's position
         */
        fun onItemLongPress(item: T, position: Int)

        /**
         * Called when a double tap occurs on a specified item.
         * @param item tapped item
         * @param position item's position
         * @return true if the event is consumed, else false
         */
        fun onDoubleTap(item: T, position: Int): Boolean
    }

    private val gestureClickListener = GestureClickListener(listener)

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {

        val childView = view.findChildViewUnder(e.x, e.y) ?: return false
        val childPosition = view.getChildAdapterPosition(childView)
        if (childPosition == RecyclerView.NO_POSITION) {
            return false
        }

        val adapter = view.adapter
        if (adapter is GestureAdapter<*, *>) {
            val gestureAdapter = adapter as GestureAdapter<T, *>
            gestureClickListener.setTouchedItem(gestureAdapter.getItem(childPosition), childPosition)
        }

        if (gestureDetector == null) {
            gestureDetector = GestureDetector(view.context, gestureClickListener)
        }

        return gestureDetector?.onTouchEvent(e) ?: false
    }
}

private class GestureClickListener<T> internal constructor(private val listener: RecyclerItemTouchListener.ItemClickListener<T>)
    : GestureDetector.SimpleOnGestureListener() {

    private var item: T? = null
    private var viewPosition = 0

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return item?.let { listener.onItemClick(it, viewPosition) } ?: false
    }

    override fun onLongPress(e: MotionEvent) {
        item?.let { listener.onItemLongPress(it, viewPosition) }
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        return item?.let { listener.onDoubleTap(it, viewPosition) } ?: false
    }

    internal fun setTouchedItem(item: T, viewPosition: Int) {
        this.item = item
        this.viewPosition = viewPosition
    }
}
