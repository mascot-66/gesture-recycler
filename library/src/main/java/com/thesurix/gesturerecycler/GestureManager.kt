package com.thesurix.gesturerecycler

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

private const val INVALID_FLAG = -1

/**
 * Class that is responsible for gesture management for [RecyclerView] widget.
 * @author thesurix
 */
class GestureManager {

    private val touchHelperCallback: GestureTouchHelperCallback

    private constructor(builder: Builder) {
        val adapter = builder.recyclerView.adapter as GestureAdapter<*, *>
        touchHelperCallback = GestureTouchHelperCallback(adapter).apply {
            swipeEnabled = builder.isSwipeEnabled
            longPressDragEnabled = builder.isDragEnabled
            manualDragEnabled = builder.isManualDragEnabled
        }

        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(builder.recyclerView)
        adapter.setGestureListener(GestureListener(touchHelper))

        if (builder.swipeFlags == INVALID_FLAG) {
            touchHelperCallback.setSwipeFlagsForLayout(builder.recyclerView.layoutManager!!)
        } else {
            touchHelperCallback.swipeFlags = builder.swipeFlags
        }

        if (builder.dragFlags == INVALID_FLAG) {
            touchHelperCallback.setDragFlagsForLayout(builder.recyclerView.layoutManager!!)
        } else {
            touchHelperCallback.dragFlags = builder.dragFlags
        }
    }

    /**
     * Returns true if swipe is enabled, false if swipe is disabled.
     * @return swipe state
     */
    /**
     * Sets swipe gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isSwipeEnabled: Boolean
        get() = touchHelperCallback.isItemViewSwipeEnabled
        set(enabled) {
            touchHelperCallback.swipeEnabled = enabled
        }

    /**
     * Returns true if long press drag is enabled, false if long press drag is disabled.
     * @return long press drag state
     */
    /**
     * Sets long press drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isLongPressDragEnabled: Boolean
        get() = touchHelperCallback.isLongPressDragEnabled
        set(enabled) {
            touchHelperCallback.longPressDragEnabled = enabled
        }

    /**
     * Returns true if manual drag is enabled, false if manual drag is disabled.
     * @return manual drag state
     */
    /**
     * Sets manual drag gesture enabled or disabled.
     * @param enabled true to enable, false to disable
     */
    var isManualDragEnabled: Boolean
        get() = touchHelperCallback.manualDragEnabled
        set(enabled) {
            touchHelperCallback.manualDragEnabled = enabled
        }

    /**
     * Class that builds [GestureManager] instance.
     * Constructs [GestureManager] for the given RecyclerView.
     * @param recyclerView RecyclerView instance
     */
    class Builder(val recyclerView: RecyclerView) {
        internal var swipeFlags = INVALID_FLAG
            private set
        internal var dragFlags = INVALID_FLAG
            private set
        internal var isSwipeEnabled = false
            private set
        internal var isDragEnabled = false
            private set
        internal var isManualDragEnabled = false
            private set

        /**
         * Sets swipe gesture enabled or disabled.
         * Swipe is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setSwipeEnabled(enabled: Boolean): Builder {
            isSwipeEnabled = enabled
            return this
        }

        /**
         * Sets long press drag gesture enabled or disabled.
         * Long press drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setLongPressDragEnabled(enabled: Boolean): Builder {
            isDragEnabled = enabled
            return this
        }

        /**
         * Sets manual drag gesture enabled or disabled.
         * Manual drag is disabled by default.
         * @param enabled true to enable, false to disable
         * @return returns builder instance
         */
        fun setManualDragEnabled(enabled: Boolean): Builder {
            isManualDragEnabled = enabled
            return this
        }

        /**
         * Sets flags for swipe and drag gesture. Do not set this flags if you want predefined flags for RecyclerView layout manager.
         * See [ItemTouchHelper] flags.
         *
         * This method is deprecated, use [.setDragFlags] or [.setSwipeFlags].
         * @param swipeFlags flags for swipe gesture
         * @param dragFlags flags for drag gesture
         * @return returns builder instance
         */
        @Deprecated("Use setSwipeFlags() and setDragFlags() methods.")
        fun setGestureFlags(swipeFlags: Int, dragFlags: Int): Builder {
            this.swipeFlags = swipeFlags
            this.dragFlags = dragFlags
            return this
        }

        /**
         * Sets flags for swipe gesture. Do not set this flags if you want predefined flags for RecyclerView layout manager.
         * See [ItemTouchHelper] flags.
         * @param flags flags for swipe gesture
         * @return returns builder instance
         */
        fun setSwipeFlags(flags: Int): Builder {
            swipeFlags = flags
            return this
        }

        /**
         * Sets flags for drag gesture. Do not set this flags if you want predefined flags for RecyclerView layout manager.
         * See [ItemTouchHelper] flags.
         * @param flags flags for drag gesture
         * @return returns builder instance
         */
        fun setDragFlags(flags: Int): Builder {
            dragFlags = flags
            return this
        }

        /**
         * Builds [GestureManager] instance.
         * @return returns GestureManager instance
         */
        fun build(): GestureManager {
            validateBuilder()
            return GestureManager(this)
        }

        private fun validateBuilder() {
            val hasAdapter = recyclerView.adapter is GestureAdapter<*, *>
            if (!hasAdapter) {
                throw IllegalArgumentException("RecyclerView does not have adapter that extends " + GestureAdapter::class.java.name)
            }

            if (swipeFlags == INVALID_FLAG || dragFlags == INVALID_FLAG) {
                if (recyclerView.layoutManager == null) {
                    throw IllegalArgumentException("No layout manager for RecyclerView. Provide custom flags or attach layout manager to RecyclerView.")
                }
            }
        }
    }

}
