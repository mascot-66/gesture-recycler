package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.SwapTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SwapTransactionTest : BaseTransactionTest() {

    @Test
    fun `move item in transaction without header`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3, false)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(1)
        Mockito.verify(transactional).notifyChanged(3)
    }

    @Test
    fun `revert move item in transaction without header`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3, false)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(3)
        Mockito.verify(transactional).notifyChanged(1)
    }

    @Test
    fun `move item in transaction with header`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3, true)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(2)
        Mockito.verify(transactional).notifyChanged(4)
    }

    @Test
    fun `revert move item in transaction with header`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3, true)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(4)
        Mockito.verify(transactional).notifyChanged(2)
    }
}