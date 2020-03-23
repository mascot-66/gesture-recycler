package com.thesurix.gesturerecycler.transactions


/**
 * @author thesurix
 */
class RevertReorderTransaction<T>(private val from: Int,
                                  private val to: Int,
                                  private val headerEnabled: Boolean) : Transaction<T> {

    override fun perform(transactional: Transactional<T>) = false

    override fun revert(transactional: Transactional<T>): Boolean {
        return with(transactional.data) {
            val item = removeAt(to)
            item?.let {
                transactional.notifyRemoved(to + if (headerEnabled) 1 else 0)
                add(from, it)
                transactional.notifyInserted(from + if (headerEnabled) 1 else 0)
                true
            } ?: false
        }
    }
}
