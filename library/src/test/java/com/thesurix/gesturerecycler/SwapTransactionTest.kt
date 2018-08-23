package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.SwapTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SwapTransactionTest : BaseTransactionTest() {

    @Test
    fun `move item in transaction`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3)

        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(1)
        Mockito.verify(transactional).notifyChanged(3)
    }

    @Test
    fun `revert move item in transaction`() {
        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3)

        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)
        Mockito.verify(transactional).notifyChanged(3)
        Mockito.verify(transactional).notifyChanged(1)
    }
}