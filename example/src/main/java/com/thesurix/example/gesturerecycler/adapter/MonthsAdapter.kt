package com.thesurix.example.gesturerecycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.databinding.GridItemBinding
import com.thesurix.example.gesturerecycler.databinding.HeaderItemBinding
import com.thesurix.example.gesturerecycler.databinding.LinearItemBinding
import com.thesurix.example.gesturerecycler.databinding.LinearItemWithBackgroundBinding
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder

class MonthsAdapter(@param:LayoutRes private val mItemResId: Int) : GestureAdapter<MonthItem, GestureViewHolder<MonthItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureViewHolder<MonthItem> {
        return if (viewType == MonthItem.MonthItemType.MONTH.ordinal) {
            when (mItemResId) {
                R.layout.linear_item -> LinearItemViewHolder(LinearItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                R.layout.linear_item_with_background -> LinearItemWithBackgroundViewHolder(
                        LinearItemWithBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                R.layout.grid_item -> GridItemViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                else -> throw UnsupportedOperationException("Unsupported resource")
            }
        } else {
            HeaderViewHolder(HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }
}
