package com.thesurix.gesturerecycler.transactions

class MoveTransaction<T>(private val from: Int,
                         private val to: Int) : Transaction<T> {
    private var item: T? = null

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(from)
            removedItem?.let {
                item = it
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
                item = it
                add(from, it)
                transactional.notifyMoved(to, from)
                true
            } ?: false
        }
    }
}