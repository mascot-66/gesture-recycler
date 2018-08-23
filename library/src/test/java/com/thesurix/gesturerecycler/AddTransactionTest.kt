package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.AddTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddTransactionTest : BaseTransactionTest() {

    @Test
    fun `add item in transaction`() {
        val transaction = AddTransaction("F")

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data.last(), "F")
        Mockito.verify(transactional).notifyInserted(data.size)
    }

    @Test
    fun `revert add item in transaction`() {
        val lastElement = transactional.data.last()
        val transaction = AddTransaction("F")
        transaction.perform(transactional)

        assertEquals(transactional.data.last(), "F")
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data.last(), lastElement)
        Mockito.verify(transactional).notifyRemoved(data.size)
    }
}