package com.thesurix.gesturerecycler.transactions

/**
 * @author thesurix
 */
interface AdapterTransaction {
    fun perform(): Boolean
    fun revert(): Boolean
}
