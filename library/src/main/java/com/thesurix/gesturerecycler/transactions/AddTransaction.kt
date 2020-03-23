package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class AddTransaction<T>(private val item: T,
                        private val headerEnabled: Boolean) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val success = add(item)
            if (success) {
                transactional.notifyInserted(if (headerEnabled) size else size - 1)
            }
            success
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(size - 1)
            item?.let {
                transactional.notifyRemoved(if (headerEnabled) size + 1 else size)
                true
            } ?: false
        }
    }
}
