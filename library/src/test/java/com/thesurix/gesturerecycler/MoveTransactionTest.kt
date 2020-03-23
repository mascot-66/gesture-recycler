package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.MoveTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoveTransactionTest : BaseTransactionTest() {

    @Test
    fun `move item in transaction without header`() {
        val item = transactional.data[1]
        val transaction = MoveTransaction<String>(1, 4, false)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], item)
        Mockito.verify(transactional).notifyMoved(1, 4)
    }

    @Test
    fun `revert move item in transaction without header`() {
        val item = transactional.data[4]
        val transaction = MoveTransaction<String>(1, 4, false)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[1], item)
        Mockito.verify(transactional).notifyMoved(4, 1)
    }

    @Test
    fun `move item in transaction with header`() {
        val item = transactional.data[1]
        val transaction = MoveTransaction<String>(1, 4, true)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], item)
        Mockito.verify(transactional).notifyMoved(2, 5)
    }

    @Test
    fun `revert move item in transaction with header`() {
        val item = transactional.data[4]
        val transaction = MoveTransaction<String>(1, 4, true)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[1], item)
        Mockito.verify(transactional).notifyMoved(5, 2)
    }
}