package com.thesurix.example.gesturerecycler.adapter;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.gesturerecycler.GestureViewHolder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HeaderViewHolder extends GestureViewHolder {

    @Bind(R.id.header_text) TextView mHeaderText;

    public HeaderViewHolder(final View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public boolean canDrag() {
        return false;
    }

    @Override
    public boolean canSwipe() {
        return false;
    }
}
