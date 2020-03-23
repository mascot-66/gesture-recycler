package com.thesurix.gesturerecycler.transactions

/**
 * @author thesurix
 */
class MoveTransaction<T>(private val from: Int,
                         private val to: Int,
                         private val headerEnabled: Boolean) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(from)
            removedItem?.let {
                add(to, it)
                val movedOffset = if (headerEnabled) 1 else 0
                transactional.notifyMoved(from + movedOffset, to + movedOffset)
                true
            } ?: false
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(to)
            removedItem?.let {
                add(from, it)
                val movedOffset = if (headerEnabled) 1 else 0
                transactional.notifyMoved(to + movedOffset, from + movedOffset)
                true
            } ?: false
        }
    }
}