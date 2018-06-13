package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

class MoveTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                         private val from: Int,
                         private val to: Int) : AdapterTransaction {
    private var item: T? = null

    override fun perform(): Boolean {
        with(adapter) {
            item = data.removeAt(from)
            val success = item != null
            if (success) {
                //TODO
                data.add(to, item as T)
                notifyItemMoved(from, to)
            }
            return success
        }
    }

    override fun revert(): Boolean {
        with(adapter) {
            item = data.removeAt(to)
            val success = item != null
            if (success) {
                //TODO
                data.add(from, item as T)
                notifyItemMoved(to, from)
            }
            return success
        }
    }
}