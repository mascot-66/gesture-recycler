package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

/**
 * @author thesurix
 */
class RevertReorderTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                                  private val from: Int,
                                  private val to: Int) : AdapterTransaction {

    override fun perform() = false


    override fun revert(): Boolean {
        return with(adapter) {
            val item = data.removeAt(to)
            item?.let {
                notifyItemRemoved(to)
                data.add(from, it)
                notifyItemInserted(from)
                true
            } ?: false
        }
    }
}
