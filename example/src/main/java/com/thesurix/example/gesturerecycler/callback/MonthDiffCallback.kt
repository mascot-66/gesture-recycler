package com.thesurix.example.gesturerecycler.callback


import androidx.recyclerview.widget.DiffUtil
import com.thesurix.example.gesturerecycler.model.MonthItem

class MonthDiffCallback(private val mOldList: List<MonthItem>, private val mNewList: List<MonthItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsEqual(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsEqual(oldItemPosition, newItemPosition)
    }

    private fun areItemsEqual(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]

        return oldItem.name == newItem.name
    }
}
