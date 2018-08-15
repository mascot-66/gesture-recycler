package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class RemoveTransaction<T>(private val position: Int) : Transaction<T> {
    private var item: T? = null

    override fun perform(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val removedItem = removeAt(position)
            removedItem?.let {
                item = it
                transactional.notifyRemoved(position)
                true
            } ?: false
        }
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            item?.let {
                add(position, it)
                transactional.notifyInserted(position)
                true
            } ?: false
        }
    }
}
