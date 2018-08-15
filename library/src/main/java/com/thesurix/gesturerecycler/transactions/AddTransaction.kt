package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class AddTransaction<T>(private val item: T) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val success = add(item)
            if (success) {
                transactional.notifyInserted(size)
            }
            success
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(size - 1)
            item?.let {
                transactional.notifyRemoved(size)
                true
            } ?: false
        }
    }
}
