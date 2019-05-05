package com.thesurix.example.gesturerecycler.adapter

import android.widget.TextView
import com.thesurix.example.gesturerecycler.databinding.HeaderItemBinding
import com.thesurix.gesturerecycler.GestureViewHolder


class HeaderViewHolder(binding: HeaderItemBinding) : GestureViewHolder(binding.root) {

    val headerText: TextView = binding.headerText

    override fun canDrag() = false

    override fun canSwipe() = false
}
