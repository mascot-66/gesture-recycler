package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class RevertReorderTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                                  private val from: Int,
                                  private val to: Int) : AdapterTransaction {

    override fun perform(): Boolean {
        return false
    }

    override fun revert(): Boolean {
        with(adapter) {
            val item = data.removeAt(to)
            if (item != null) {
                notifyItemRemoved(to)
                data.add(from, item)
                notifyItemInserted(from)
                return true
            }
        }

        return false
    }
}
