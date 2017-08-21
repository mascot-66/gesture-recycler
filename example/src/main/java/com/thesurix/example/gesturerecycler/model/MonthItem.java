package com.thesurix.example.gesturerecycler.model;

public interface MonthItem {

    enum MonthItemType {
        HEADER, MONTH
    }

    MonthItemType getType();

    String getName();
}
