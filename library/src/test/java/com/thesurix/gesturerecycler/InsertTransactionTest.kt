package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.InsertTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertTransactionTest : BaseTransactionTest() {

    @Test
    fun `insert item in transaction without header`() {
        val itemToInsert = "X"
        val transaction = InsertTransaction(itemToInsert, 4, false)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], itemToInsert)
        Mockito.verify(transactional).notifyInserted(4)
    }

    @Test
    fun `revert insert item in transaction without header`() {
        val itemToInsert = "X"
        val itemBeforeInsert = transactional.data[4]
        val transaction = InsertTransaction(itemToInsert, 4, false)
        transaction.perform(transactional)

        assertEquals(transactional.data[4], itemToInsert)
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[4], itemBeforeInsert)
        Mockito.verify(transactional).notifyRemoved(4)
    }

    @Test
    fun `insert item in transaction with header`() {
        val itemToInsert = "X"
        val transaction = InsertTransaction(itemToInsert, 4, true)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], itemToInsert)
        Mockito.verify(transactional).notifyInserted(5)
    }

    @Test
    fun `revert insert item in transaction with header`() {
        val itemToInsert = "X"
        val itemBeforeInsert = transactional.data[4]
        val transaction = InsertTransaction(itemToInsert, 4, true)
        transaction.perform(transactional)

        assertEquals(transactional.data[4], itemToInsert)
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[4], itemBeforeInsert)
        Mockito.verify(transactional).notifyRemoved(5)
    }
}