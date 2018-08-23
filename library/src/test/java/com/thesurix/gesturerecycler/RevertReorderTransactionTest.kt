package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RevertReorderTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RevertReorderTransactionTest : BaseTransactionTest() {

    @Test
    fun `reorder item in transaction`() {
        val transaction = RevertReorderTransaction<String>(3, 4)

        assertFalse(transaction.perform(transactional))
    }

    @Test
    fun `revert reorder item in transaction`() {
        val item1 = transactional.data[3]
        val item2 = transactional.data[4]
        val transaction = RevertReorderTransaction<String>(3, 4)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[3], item2)
        assertEquals(transactional.data[4], item1)
        Mockito.verify(transactional).notifyRemoved(4)
        Mockito.verify(transactional).notifyInserted(3)
    }
}