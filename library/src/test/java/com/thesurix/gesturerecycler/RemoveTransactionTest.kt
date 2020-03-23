package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RemoveTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveTransactionTest : BaseTransactionTest() {

    @Test
    fun `remove item in transaction without header`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3, false)

        assertTrue(transaction.perform(transactional))
        assertFalse(transactional.data.contains(item))
        Mockito.verify(transactional).notifyRemoved(3)
    }

    @Test
    fun `revert remove item in transaction without header`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3, false)
        transaction.perform(transactional)

        assertFalse(transactional.data.contains(item))
        assertTrue(transaction.revert(transactional))
        assertTrue(transactional.data.contains(item))
        Mockito.verify(transactional).notifyInserted(3)
    }

    @Test
    fun `remove item in transaction with header`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3, true)

        assertTrue(transaction.perform(transactional))
        assertFalse(transactional.data.contains(item))
        Mockito.verify(transactional).notifyRemoved(4)
    }

    @Test
    fun `revert remove item in transaction with header`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3, true)
        transaction.perform(transactional)

        assertFalse(transactional.data.contains(item))
        assertTrue(transaction.revert(transactional))
        assertTrue(transactional.data.contains(item))
        Mockito.verify(transactional).notifyInserted(4)
    }
}