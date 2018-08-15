package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.Transactional
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseTransactionTest {

    @Mock
    lateinit var transactional: Transactional<String>

    val data = mutableListOf("A", "B", "C", "D", "E")

    @Before
    fun setUp() {
        Mockito.`when`(transactional.data).thenReturn(data)
    }
}