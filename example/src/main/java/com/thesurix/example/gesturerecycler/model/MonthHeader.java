package com.thesurix.example.gesturerecycler.model;

public class MonthHeader implements MonthItem {

    private String mName;

    public MonthHeader(final String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    @Override
    public MonthItemType getType() {
        return MonthItemType.HEADER;
    }
}
