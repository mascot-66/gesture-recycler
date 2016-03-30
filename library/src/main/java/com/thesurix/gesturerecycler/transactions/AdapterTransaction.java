package com.thesurix.gesturerecycler.transactions;


public interface AdapterTransaction {

    boolean perform();

    boolean revert();
}
