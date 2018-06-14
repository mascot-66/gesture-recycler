package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class RemoveTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                           private val position: Int) : AdapterTransaction {
    private var item: T? = null

    override fun perform(): Boolean {
        return with(adapter) {
            val removedItem = data.removeAt(position)
            removedItem?.let {
                item = it
                notifyItemRemoved(position)
                true
            } ?: false
        }
    }

    override fun revert(): Boolean {
        return with(adapter) {
            item?.let {
                data.add(position, it)
                notifyItemInserted(position)
                 true
            } ?: false
        }
    }
}
