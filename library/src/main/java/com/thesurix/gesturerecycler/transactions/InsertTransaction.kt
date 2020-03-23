package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class InsertTransaction<T>(private val item: T,
                           private val position: Int,
                           private val headerEnabled: Boolean) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            add(position, item)
            val insertedPosition = position + if (headerEnabled) 1 else 0
            transactional.notifyInserted(insertedPosition)
            true
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(position)
            item?.let {
                val removedPosition = position + if (headerEnabled) 1 else 0
                transactional.notifyRemoved(removedPosition)
                true
            } ?: false
        }
    }
}
