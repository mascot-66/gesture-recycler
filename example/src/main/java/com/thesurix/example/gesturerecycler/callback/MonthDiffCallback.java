package com.thesurix.example.gesturerecycler.callback;


import com.thesurix.example.gesturerecycler.model.MonthItem;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class MonthDiffCallback extends DiffUtil.Callback {

    private final List<MonthItem> mOldList;
    private final List<MonthItem> mNewList;

    public MonthDiffCallback(final List<MonthItem> oldList, final List<MonthItem> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList == null ? 0 : mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return areItemsEqual(oldItemPosition, newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return areItemsEqual(oldItemPosition, newItemPosition);
    }

    private boolean areItemsEqual(final int oldItemPosition, final int newItemPosition) {
        final MonthItem oldItem = mOldList.get(oldItemPosition);
        final MonthItem newItem = mNewList.get(newItemPosition);

        return oldItem.getName().equals(newItem.getName());
    }
}
