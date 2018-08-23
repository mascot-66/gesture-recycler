package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RemoveTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveTransactionTest : BaseTransactionTest() {

    @Test
    fun `remove item in transaction`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3)

        assertTrue(transaction.perform(transactional))
        assertFalse(transactional.data.contains(item))
        Mockito.verify(transactional).notifyRemoved(3)
    }

    @Test
    fun `revert remove item in transaction`() {
        val item = transactional.data[3]
        val transaction = RemoveTransaction<String>(3)
        transaction.perform(transactional)

        assertFalse(transactional.data.contains(item))
        assertTrue(transaction.revert(transactional))
        assertTrue(transactional.data.contains(item))
        Mockito.verify(transactional).notifyInserted(3)
    }
}