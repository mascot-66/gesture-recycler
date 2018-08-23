package com.thesurix.gesturerecycler

import android.support.v4.view.MotionEventCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.thesurix.gesturerecycler.transactions.*
import com.thesurix.gesturerecycler.util.FixedSizeArrayDequeue
import java.util.*

private const val INVALID_DRAG_POS = -1

/**
 * Base adapter for gesture recognition, extends this to provide own implementation. T is the data type, K is the ViewHolder type.
 * @author thesurix
 */
abstract class GestureAdapter<T, K : GestureViewHolder> : RecyclerView.Adapter<K>(), Transactional<T> {

    /** Temp item for swap action  */
    private var swappedItem: T? = null
    /** Start position of the drag action  */
    private var startDragPos = 0
    /** Stop position of the drag action  */
    private var stopDragPos = INVALID_DRAG_POS
    /** Flag that defines if adapter allows manual dragging  */
    private var manualDragAllowed = false
    /** This variable holds stack of data transactions for undo purposes  */
    private var transactions = FixedSizeArrayDequeue<Transaction<T>>(1)

    private var gestureListener: OnGestureListener? = null
    private var dataChangeListener: OnDataChangeListener<T>? = null
    private val emptyViewDataObserver = EmptyViewDataObserver()
    private val attachListener = object : View.OnAttachStateChangeListener {

        private var registered = false

        override fun onViewAttachedToWindow(v: View) {
            if (!registered) {
                registered = true
                registerAdapterDataObserver(emptyViewDataObserver)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            if (registered) {
                registered = false
                unregisterAdapterDataObserver(emptyViewDataObserver)
            }
            resetTransactions()
        }
    }

    /** Collection for adapter's data  */
    private val _data = ArrayList<T>()

    /**
     * Returns adapter's data.
     * @return adapter's data
     */
    /**
     * Sets adapter data. This method will interrupt pending animations.
     * Use [.add], [.remove] or [.insert] or [.setData] to achieve smooth animations.
     * @param data data to show
     */

    override var data: MutableList<T>
        get() = _data
        set(data) = setData(data, null)

    /** Listener for data changes inside adapter  */
    interface OnDataChangeListener<T> {

        /**
         * Called when item has been removed by swipe gesture.
         * @param item removed item
         * @param position removed position
         */
        fun onItemRemoved(item: T, position: Int)

        /**
         * Called when item has been reordered by drag gesture.
         * @param item reordered item
         * @param fromPos reorder start position
         * @param toPos reorder end position
         */
        fun onItemReorder(item: T, fromPos: Int, toPos: Int)
    }

    /** Listener for gestures  */
    internal interface OnGestureListener {

        /**
         * Called when view holder item has pending drag gesture.
         * @param viewHolder dragged view holder item
         */
        fun onStartDrag(viewHolder: GestureViewHolder)
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        holder.draggableView?.let {
            if (manualDragAllowed && holder.canDrag()) {
                holder.showDraggableView()
                holder.draggableView?.setOnTouchListener { _, motionEvent ->
                    if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                        gestureListener?.onStartDrag(holder)
                    }

                    false
                }
            } else {
                holder.hideDraggableView()
            }
        }
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        emptyViewDataObserver.recyclerView = recyclerView
        recyclerView.addOnAttachStateChangeListener(attachListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        emptyViewDataObserver.recyclerView = null
        recyclerView.removeOnAttachStateChangeListener(attachListener)
        resetTransactions()
    }

    override fun notifyChanged(position: Int) {
        notifyItemChanged(position)
    }

    override fun notifyInserted(position: Int) {
        notifyItemInserted(position)
    }

    override fun notifyRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    override fun notifyMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    /**
     * Sets adapter data with [DiffUtil.Callback] to achieve smooth animations.
     * @param data data to show
     * @param diffCallback diff callback to manage internal data changes
     */
    fun setData(data: List<T>, diffCallback: DiffUtil.Callback?) {
        when(diffCallback) {
            null -> {
                setNewData(data)
                notifyDataSetChanged()
            }
            else -> {
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                setNewData(data)
                diffResult.dispatchUpdatesTo(this)
            }
        }
        resetTransactions()
    }

    /**
     * Clears data.
     */
    fun clearData() {
        _data.clear()
        notifyDataSetChanged()

        resetTransactions()
    }

    /**
     * Returns item for the given position
     * @param position item's position
     * @return item
     */
    fun getItem(position: Int): T {
        return _data[position]
    }

    /**
     * Adds item to the adapter.
     * @param item item to add
     * @return true if added, false otherwise
     */
    fun add(item: T): Boolean {
        val addTransaction = AddTransaction(item)
        val success = addTransaction.perform(this)

        transactions.offer(addTransaction)
        return success
    }

    /**
     * Removes item from the given position.
     * @param position item's position
     * @return true if removed, false otherwise
     */
    fun remove(position: Int): Boolean {
        val removeTransaction = RemoveTransaction<T>(position)
        val success = removeTransaction.perform(this)

        transactions.offer(removeTransaction)
        return success
    }

    /**
     * Inserts item in the given position.
     * @param item item to insert
     * @param position position for the item
     */
    fun insert(item: T, position: Int) {
        val insertTransaction = InsertTransaction(item, position)
        insertTransaction.perform(this)

        transactions.offer(insertTransaction)
    }

    /**
     * Moves item from one position to another.
     * @param fromPosition item's old position
     * @param toPosition item's new position
     * @return true if moved, false otherwise
     */
    fun move(fromPosition: Int, toPosition: Int): Boolean {
        val moveTransaction = MoveTransaction<T>(fromPosition, toPosition)
        val success = moveTransaction.perform(this)

        transactions.offer(moveTransaction)
        return success
    }

    /**
     * Swap items in given positions.
     * @param firstPosition first item position
     * @param secondPosition second item position
     * @return true if swapped, false otherwise
     */
    fun swap(firstPosition: Int, secondPosition: Int): Boolean {
        val swapTransaction = SwapTransaction<T>(firstPosition, secondPosition)
        val success = swapTransaction.perform(this)

        transactions.offer(swapTransaction)
        return success
    }

    /**
     * Sets empty view. Empty view is used when adapter has no data.
     * Pass null to disable empty view feature.
     * @param emptyView view to show
     */
    fun setEmptyView(emptyView: View) {
        emptyViewDataObserver.emptyView = emptyView
    }

    /**
     * Sets empty view visibility listener.
     * @param listener empty view visibility listener
     */
    fun setEmptyViewVisibilityListener(listener: EmptyViewVisibilityListener) {
        emptyViewDataObserver.emptyViewVisibilityListener = listener
    }

    /**
     * Sets undo stack size. If undo stack is full, the oldest action will be removed (default size is 1).
     * @param size undo actions size
     */
    fun setUndoSize(size: Int) {
        if (size < 0) {
            throw IllegalArgumentException("Stack can not have negative size.")
        }
        transactions = FixedSizeArrayDequeue(size)
    }

    /**
     * Reverts last data transaction like [.add], [.remove],
     * [.insert]. It supports also reverting swipe and drag & drop actions.
     *
     * @return true for successful undo action, false otherwise
     */
    fun undoLast(): Boolean {
        return transactions.isNotEmpty() && transactions.pollLast().revert(this)
    }

    /**
     * Sets adapter data change listener.
     * @param listener data change listener
     */
    fun setDataChangeListener(listener: OnDataChangeListener<T>) {
        dataChangeListener = listener
    }

    /**
     * Sets adapter gesture listener.
     * @param listener gesture listener
     */
    internal fun setGestureListener(listener: OnGestureListener) {
        gestureListener = listener
    }

    /**
     * Dismisses item from the given position.
     * @param position item's position
     */
    internal fun onItemDismissed(position: Int) {
        val removedItem = _data[position]
        val wasRemoved = remove(position)
        if (wasRemoved) {
            dataChangeListener?.onItemRemoved(removedItem, position)
        }
    }

    /**
     * Moves item from one position to another.
     * @param fromPosition start position
     * @param toPosition end position
     * @return returns true if transition is successful
     */
    internal fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (swappedItem == null) {
            startDragPos = fromPosition
            swappedItem = _data[fromPosition]
        }
        stopDragPos = toPosition

        // Steps bigger than one we have to swap manually in right order
        val jumpSize = Math.abs(toPosition - fromPosition)
        if (jumpSize > 1) {
            val sign = Integer.signum(toPosition - fromPosition)
            var startPos = fromPosition
            for (i in 0 until jumpSize) {
                val endPos = startPos + sign
                Collections.swap(_data, startPos, endPos)
                startPos += sign
            }
        } else {
            Collections.swap(_data, fromPosition, toPosition)
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    /**
     * Called when item has been moved.
     */
    internal fun onItemMoved() {
        swappedItem?.let {
            if (stopDragPos != INVALID_DRAG_POS) {
                dataChangeListener?.onItemReorder(it, startDragPos, stopDragPos)

                val revertReorderTransaction = RevertReorderTransaction<T>(startDragPos, stopDragPos)
                transactions.offer(revertReorderTransaction)
                swappedItem = null
                stopDragPos = INVALID_DRAG_POS
            }
        }
    }

    /**
     * Enables or disables manual drag actions on items. Manual dragging is disabled by default.
     * To allow manual drags provide draggable view, see [GestureViewHolder].
     * @param allowState true to enable, false to disable
     */
    internal fun allowManualDrag(allowState: Boolean) {
        manualDragAllowed = allowState
        notifyDataSetChanged()
    }

    private fun setNewData(data: List<T>) {
        _data.clear()
        _data.addAll(data)
    }

    private fun resetTransactions() {
        transactions.clear()
    }
}
