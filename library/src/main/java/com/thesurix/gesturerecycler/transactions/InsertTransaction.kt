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
        return with(adapter) {
            data.add(position, item)
            notifyItemInserted(position)
            true
        }
    }

    override fun revert(): Boolean {
        return with(adapter) {
            val item = data.removeAt(position)
            item?.let {
                notifyItemRemoved(position)
                true
            } ?: false
        }
    }
}
