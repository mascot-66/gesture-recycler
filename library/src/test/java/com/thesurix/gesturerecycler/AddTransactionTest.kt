package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.AddTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddTransactionTest : BaseTransactionTest() {

    @Test
    fun `add item in transaction without header`() {
        val transaction = AddTransaction("F", false)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data.last(), "F")
        Mockito.verify(transactional).notifyInserted(data.size - 1)
    }

    @Test
    fun `revert add item in transaction without header`() {
        val lastElement = transactional.data.last()
        val transaction = AddTransaction("F", false)
        transaction.perform(transactional)

        assertEquals(transactional.data.last(), "F")
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data.last(), lastElement)
        Mockito.verify(transactional).notifyRemoved(data.size)
    }

    @Test
    fun `add item in transaction with header`() {
        val transaction = AddTransaction("F", true)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data.last(), "F")
        Mockito.verify(transactional).notifyInserted(data.size)
    }

    @Test
    fun `revert add item in transaction with header`() {
        val lastElement = transactional.data.last()
        val transaction = AddTransaction("F", true)
        transaction.perform(transactional)

        assertEquals(transactional.data.last(), "F")
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data.last(), lastElement)
        Mockito.verify(transactional).notifyRemoved(data.size + 1)
    }
}