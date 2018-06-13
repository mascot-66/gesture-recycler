package com.thesurix.gesturerecycler


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Enum with predefined gesture flags for various layout managers, see [RecyclerView.LayoutManager]
 * @author thesurix
 */
internal enum class LayoutFlags {
    LINEAR {
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            val linearLayout = layout as LinearLayoutManager
            return when(linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                else -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val linearLayout = layout as LinearLayoutManager
            return when(linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.UP
                else -> ItemTouchHelper.RIGHT
            }
        }
    },
    GRID {
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val gridLayout = layout as GridLayoutManager
            return when(gridLayout.orientation) {
                GridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> ItemTouchHelper.RIGHT
            }
        }
    },
    STAGGERED {
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val staggeredGridLayout = layout as StaggeredGridLayoutManager
            return when(staggeredGridLayout.orientation) {
                StaggeredGridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> ItemTouchHelper.RIGHT
            }
        }
    };

    /**
     * Returns drag flags for the given layout manager.
     * @param layout layout manager instance
     * @return drag flags
     */
    internal abstract fun getDragFlags(layout: RecyclerView.LayoutManager): Int

    /**
     * Returns swipe flags for the given layout manager.
     * @param layout layout manager instance
     * @return swipe flags
     */
    internal abstract fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int
}
