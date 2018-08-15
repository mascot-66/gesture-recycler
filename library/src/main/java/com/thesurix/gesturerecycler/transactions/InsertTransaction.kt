package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class InsertTransaction<T>(private val item: T,
                           private val position: Int) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            add(position, item)
            transactional.notifyInserted(position)
            true
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(position)
            item?.let {
                transactional.notifyRemoved(position)
                true
            } ?: false
        }
    }
}
