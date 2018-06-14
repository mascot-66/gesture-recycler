package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class AddTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                        private val item: T) : AdapterTransaction {

    override fun perform(): Boolean {
        return with(adapter) {
            val success = data.add(item)
            if (success) {
                notifyItemInserted(itemCount)
            }
            success
        }
    }

    override fun revert(): Boolean {
        return with(adapter) {
            val dataSize = itemCount
            val item = data.removeAt(dataSize - 1)
            item?.let {
                notifyItemRemoved(dataSize)
                true
            } ?: false
        }
    }
}
