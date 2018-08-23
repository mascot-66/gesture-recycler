package com.thesurix.gesturerecycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Observer class for managing visibility of the adapter's empty view.
 */
internal class EmptyViewDataObserver : RecyclerView.AdapterDataObserver() {

    var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }
    var emptyView: View? = null
        set(value) {
            field = value
            updateEmptyViewState()
        }

    var emptyViewVisibilityListener: EmptyViewVisibilityListener? = null

    override fun onChanged() {
        updateEmptyViewState()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        updateEmptyViewState()
    }

    private fun updateEmptyViewState() {
        recyclerView?.let {
            val noItems = it.adapter?.itemCount == 0
            emptyView?.visibility = if (noItems) View.VISIBLE else View.GONE
            recyclerView?.visibility = if (noItems) View.GONE else View.VISIBLE
            emptyViewVisibilityListener?.onVisibilityChanged(noItems)
        }
    }
}

interface EmptyViewVisibilityListener {
    fun onVisibilityChanged(visible: Boolean)
}
