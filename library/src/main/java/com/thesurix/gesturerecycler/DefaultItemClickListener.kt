package com.thesurix.gesturerecycler

/**
 * Default implementation of the [RecyclerItemTouchListener.ItemClickListener].
 * @author thesurix
 */
open class DefaultItemClickListener<T> : RecyclerItemTouchListener.ItemClickListener<T> {

    override fun onItemClick(item: T, position: Int): Boolean {
        return false
    }

    override fun onItemLongPress(item: T, position: Int) {
    }

    override fun onDoubleTap(item: T, position: Int): Boolean {
        return false
    }
}
