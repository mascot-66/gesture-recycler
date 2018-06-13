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
        if (recyclerView?.adapter?.itemCount == 0) {
            emptyView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyView?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }
}
