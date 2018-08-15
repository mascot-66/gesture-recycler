package com.thesurix.gesturerecycler.transactions

/**
 * @author thesurix
 */
interface Transaction<T> {
    fun perform(transactional: Transactional<T>): Boolean
    fun revert(transactional: Transactional<T>): Boolean
}