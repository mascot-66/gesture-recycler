package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class InsertTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                           private val item: T,
                           private val position: Int) : AdapterTransaction {

    override fun perform(): Boolean {
        with(adapter) {
            data.add(position, item)
            notifyItemInserted(position)
        }
        return true
    }

    override fun revert(): Boolean {
        with(adapter) {
            val item = data.removeAt(position)
            val success = item != null
            if (success) {
                notifyItemRemoved(position)
            }
            return success
        }
    }
}
