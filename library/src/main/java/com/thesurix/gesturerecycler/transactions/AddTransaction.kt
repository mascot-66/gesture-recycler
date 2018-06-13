package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class AddTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                        private val item: T) : AdapterTransaction {

    override fun perform(): Boolean {
        with(adapter) {
            val success = data.add(item)
            if (success) {
                notifyItemInserted(adapter.itemCount)
            }
            return success
        }
    }

    override fun revert(): Boolean {
        with(adapter) {
            val dataSize = itemCount
            val item = data.removeAt(dataSize - 1)
            val success = item != null
            if (success) {
                notifyItemRemoved(dataSize)
            }
            return success
        }
    }
}
