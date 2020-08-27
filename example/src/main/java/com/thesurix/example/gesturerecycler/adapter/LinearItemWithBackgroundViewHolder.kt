package com.thesurix.example.gesturerecycler.adapter

import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import com.thesurix.example.gesturerecycler.databinding.LinearItemWithBackgroundBinding

class LinearItemWithBackgroundViewHolder(private val binding: LinearItemWithBackgroundBinding) : BaseMonthViewHolder(binding.root) {

    override val monthText: TextView
        get() = binding.monthText
    override val monthPicture: ImageView
        get() = binding.monthImage
    override val itemDrag: ImageView
        get() = binding.monthDrag
    override val foreground: View?
        get() = binding.foreground

    override fun getBackgroundView(direction: Int): View? {
        if (direction == ItemTouchHelper.RIGHT) {
            return binding.monthBackgroundTwo
        }
        return binding.monthBackgroundOne
    }
}