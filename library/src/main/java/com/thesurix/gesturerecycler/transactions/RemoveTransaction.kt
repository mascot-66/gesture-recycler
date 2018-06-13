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
        with(adapter) {
            item = data.removeAt(position)
            val success = item != null
            if (success) {
                notifyItemRemoved(position)
            }
            return success
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
