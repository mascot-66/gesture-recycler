package com.thesurix.gesturerecycler.transactions


import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

class MoveTransaction<T>(private val adapter: GestureAdapter<T, out GestureViewHolder>,
                         private val from: Int,
                         private val to: Int) : AdapterTransaction {
    private var item: T? = null

    override fun perform(): Boolean {
        return with(adapter) {
            val removedItem = data.removeAt(from)
            removedItem?.let {
                item = it
                data.add(to, it)
                notifyItemMoved(from, to)
                true
            } ?: false
        }
    }

    override fun revert(): Boolean {
        return with(adapter) {
            val removedItem = data.removeAt(to)
            removedItem?.let {
                item = it
                data.add(from, it)
                notifyItemMoved(to, from)
                true
            } ?: false
        }
    }
}