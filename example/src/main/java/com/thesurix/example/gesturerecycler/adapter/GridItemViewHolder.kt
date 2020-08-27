package com.thesurix.example.gesturerecycler.adapter

import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.thesurix.example.gesturerecycler.databinding.GridItemBinding

class GridItemViewHolder (private val binding: GridItemBinding) : BaseMonthViewHolder(binding.root) {
    override val monthText: TextView
        get() = binding.monthText
    override val monthPicture: ImageView
        get() = binding.monthImage
    override val itemDrag: ImageView
        get() = binding.monthDrag
    override val foreground: View?
        get() = null
}