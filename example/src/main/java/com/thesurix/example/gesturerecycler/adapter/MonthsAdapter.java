package com.thesurix.example.gesturerecycler.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.model.Month;
import com.thesurix.example.gesturerecycler.model.MonthHeader;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(mItemResId, parent, false);
            return new MonthViewHolder(itemView);
        } else {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final GestureViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final MonthItem monthItem = getItem(position);

        if (monthItem.getType() == MonthItem.MonthItemType.MONTH) {
            final MonthViewHolder monthViewHolder = (MonthViewHolder) holder;
            final Month month = (Month) monthItem;
            monthViewHolder.mMonthText.setText(month.getName());

            Glide.with(mCtx).load(month.getDrawableId()).apply(RequestOptions.centerCropTransform()).into(monthViewHolder.mMonthPicture);
        } else {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            final MonthHeader monthHeader = (MonthHeader) monthItem;
            headerViewHolder.mHeaderText.setText(monthHeader.getName());
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getType().ordinal();
    }
}
