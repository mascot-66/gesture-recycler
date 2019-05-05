package com.thesurix.example.gesturerecycler.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.databinding.GridItemBinding;
import com.thesurix.example.gesturerecycler.databinding.HeaderItemBinding;
import com.thesurix.example.gesturerecycler.databinding.LinearItemBinding;
import com.thesurix.example.gesturerecycler.databinding.LinearItemWithBackgroundBinding;
import com.thesurix.example.gesturerecycler.model.Month;
import com.thesurix.example.gesturerecycler.model.MonthHeader;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public class MonthsAdapter extends GestureAdapter<MonthItem, GestureViewHolder> {

    private final Context mCtx;
    private final int mItemResId;

    public MonthsAdapter(final Context ctx, @LayoutRes final int itemResId) {
        mCtx = ctx;
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
    public void onBindViewHolder(final GestureViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final MonthItem monthItem = getItem(position);

        if (monthItem.getType() == MonthItem.MonthItemType.MONTH) {
            final BaseMonthViewHolder monthViewHolder = (BaseMonthViewHolder) holder;
            final Month month = (Month) monthItem;
            monthViewHolder.getMonthText().setText(month.getName());

            Glide.with(mCtx)
                    .load(month.getDrawableId())
                    .apply(RequestOptions.centerCropTransform())
                    .into(monthViewHolder.getMonthPicture());
        } else {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            final MonthHeader monthHeader = (MonthHeader) monthItem;
            headerViewHolder.getHeaderText().setText(monthHeader.getName());
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getType().ordinal();
    }
}
