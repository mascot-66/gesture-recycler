package com.thesurix.example.gesturerecycler.adapter;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.databinding.GridItemBinding;
import com.thesurix.example.gesturerecycler.databinding.HeaderItemBinding;
import com.thesurix.example.gesturerecycler.databinding.LinearItemBinding;
import com.thesurix.example.gesturerecycler.databinding.LinearItemWithBackgroundBinding;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public class MonthsAdapter extends GestureAdapter<MonthItem, GestureViewHolder<MonthItem>> {

    private final int mItemResId;

    public MonthsAdapter(@LayoutRes final int itemResId) {
        mItemResId = itemResId;
    }

    @Override
    public GestureViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == MonthItem.MonthItemType.MONTH.ordinal()) {
            switch (mItemResId) {
                case R.layout.linear_item:
                    return new LinearItemViewHolder(LinearItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case R.layout.linear_item_with_background:
                    return new LinearItemWithBackgroundViewHolder(
                            LinearItemWithBackgroundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case R.layout.grid_item:
                    return new GridItemViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                default:
                    throw new UnsupportedOperationException("Unsupported resource");
            }
        } else {
            return new HeaderViewHolder(HeaderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getType().ordinal();
    }
}
