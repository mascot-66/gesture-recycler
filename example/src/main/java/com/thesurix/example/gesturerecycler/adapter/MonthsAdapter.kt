package com.thesurix.example.gesturerecycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.databinding.*
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder
import com.thesurix.gesturerecycler.TYPE_FOOTER_ITEM
import com.thesurix.gesturerecycler.TYPE_HEADER_ITEM

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
        } else if (viewType == TYPE_HEADER_ITEM) {
            createHeaderOrFooterViewHolder(parent.context, parent, R.layout.header_item)
        } else if (viewType == TYPE_FOOTER_ITEM) {
            createHeaderOrFooterViewHolder(parent.context, parent, R.layout.footer_item)
        } else {
            MonthHeaderViewHolder(MonthHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(viewPosition: Int): Int {
        val handledType = super.getItemViewType(viewPosition)
        if (handledType > 0) {
            return handledType
        }
        return getItemByViewPosition(viewPosition).type.ordinal
    }

    fun createHeaderOrFooterViewHolder(context: Context, parent: ViewGroup, @LayoutRes layout: Int): GestureViewHolder<MonthItem> {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return object : GestureViewHolder<MonthItem>(view) {
            override fun canDrag() = false

            override fun canSwipe() = false
        }
    }
}
