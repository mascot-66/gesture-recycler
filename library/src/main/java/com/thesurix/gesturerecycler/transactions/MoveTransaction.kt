package com.thesurix.gesturerecycler.transactions

/**
 * @author thesurix
 */
class MoveTransaction<T>(private val from: Int,
                         private val to: Int) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(from)
            removedItem?.let {
                add(to, it)
                transactional.notifyMoved(from, to)
                true
            } ?: false
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(to)
            removedItem?.let {
                add(from, it)
                transactional.notifyMoved(to, from)
                true
            } ?: false
        }
    }
}