package com.thesurix.gesturerecycler

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thesurix.gesturerecycler.transactions.*
import com.thesurix.gesturerecycler.util.FixedSizeArrayDequeue
import com.thesurix.gesturerecycler.util.getDataOffset
import java.util.*
import kotlin.math.abs

/**
 * Base adapter for gesture recognition, extends this to provide own implementation. T is the data type, K is the ViewHolder type.
 * @author thesurix
 */
private const val INVALID_DRAG_POS = -1
const val TYPE_HEADER_ITEM = 456789
const val TYPE_FOOTER_ITEM = TYPE_HEADER_ITEM + 1
abstract class GestureAdapter<T, K : GestureViewHolder<T>> : RecyclerView.Adapter<K>(), Transactional<T> {

    /** Temp item for swap action  */
    private var swappedItem: T? = null
    /** Start position of the drag action  */
    private var startDragPos = 0
    /** Stop position of the drag action  */
    private var stopDragPos = INVALID_DRAG_POS
    /** Flag that defines if adapter allows manual dragging  */
    private var manualDragAllowed = false
    /** Flag that defines if header item is enabled */
    private var headerEnabled = false
    /** Flag that defines if footer item is enabled */
    private var footerEnabled = false
    /** This variable holds stack of data transactions for undo purposes  */
    private var transactions = FixedSizeArrayDequeue<Transaction<T>>(1)

    private var gestureListener: OnGestureListener<T>? = null
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
    private val _data = mutableListOf<T>()

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
         * @param direction the direction to which the ViewHolder is swiped. See [androidx.recyclerview.widget.ItemTouchHelper]
         */
        fun onItemRemoved(item: T, position: Int, direction: Int)

        /**
         * Called when item has been reordered by drag gesture.
         * @param item reordered item
         * @param fromPos reorder start position
         * @param toPos reorder end position
         */
        fun onItemReorder(item: T, fromPos: Int, toPos: Int)
    }

    /** Listener for gestures  */
    internal interface OnGestureListener<T> {

        /**
         * Called when view holder item has pending drag gesture.
         * @param viewHolder dragged view holder item
         */
        fun onStartDrag(viewHolder: GestureViewHolder<T>)
    }

    override fun getItemViewType(viewPosition: Int): Int {
        if (headerEnabled && viewPosition == 0) {
            return TYPE_HEADER_ITEM
        }

        val dataSize = if (headerEnabled) _data.size + 1 else _data.size
        if (footerEnabled && viewPosition == dataSize) {
            return TYPE_FOOTER_ITEM
        }
        return super.getItemViewType(viewPosition)
    }

    override fun onBindViewHolder(holder: K, position: Int, payloads: MutableList<Any>) {
        val viewType = getItemViewType(position)
        if (viewType == TYPE_HEADER_ITEM || viewType == TYPE_FOOTER_ITEM) {
            return
        }

        holder.bind(getItemByViewPosition(position))
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        holder.draggableView?.let {
            if (manualDragAllowed && holder.canDrag()) {
                holder.showDraggableView()
                holder.draggableView?.setOnTouchListener { _, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        gestureListener?.onStartDrag(holder)
                    }

                    false
                }
            } else {
                holder.hideDraggableView()
            }
        }
    }

    override fun onViewRecycled(holder: K) {
        if (holder.isRecyclable) {
            holder.recycle()
        }
    }

    override fun getItemCount(): Int {
        return when {
            headerEnabled && footerEnabled -> _data.size + 2
            headerEnabled || footerEnabled -> _data.size + 1
            else -> _data.size
        }
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
     * Returns item for the given view position.
     * @param position view position
     * @return item
     */
    fun getItemByViewPosition(position: Int): T {
        val dataPosition = position + getDataOffset(headerEnabled)
        return _data[dataPosition]
    }

    /**
     * Adds item to the adapter.
     * @param item item to add
     * @return true if added, false otherwise
     */
    fun add(item: T): Boolean {
        val addTransaction = AddTransaction(item, headerEnabled)
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
        val removeTransaction = RemoveTransaction<T>(position, headerEnabled)
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
        val insertTransaction = InsertTransaction(item, position, headerEnabled)
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
        val moveTransaction = MoveTransaction<T>(fromPosition, toPosition, headerEnabled)
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
        val swapTransaction = SwapTransaction<T>(firstPosition, secondPosition, headerEnabled)
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
        require(size >= 0) { "Stack can not have negative size." }
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
     * Sets header item state.
     * @param enabled true to enable, false to disable
     */
    fun setHeaderEnabled(enabled: Boolean) {
        if (headerEnabled != enabled) {
            headerEnabled = enabled
            notifyDataSetChanged()
        }
    }

    /**
     * Sets footer item state.
     * @param enabled true to enable, false to disable
     */
    fun setFooterEnabled(enabled: Boolean) {
        if (footerEnabled != enabled) {
            footerEnabled = enabled
            notifyDataSetChanged()
        }
    }

    /**
     * Defines if move to position toPosition is allowed. E.g. it can restrict moves within group
     * or deny move over some element that cannot be declared neither header nor footer.
     * @param fromPosition view start position
     * @param toPosition view end position
     * @return returns true if transition is allowed
     */
    open fun isItemMoveAllowed(fromPosition: Int, toPosition: Int): Boolean = true

    /**
     * Sets adapter gesture listener.
     * @param listener gesture listener
     */
    internal fun setGestureListener(listener: OnGestureListener<T>) {
        gestureListener = listener
    }

    /**
     * Dismisses item from the given position.
     * @param viewPosition dismissed item position
     * @param direction the direction to which the ViewHolder is swiped
     */
    internal fun onItemDismissed(viewPosition: Int, direction: Int) {
        val dataRemovePosition = viewPosition + getDataOffset(headerEnabled)
        val removedItem = _data[dataRemovePosition]
        val wasRemoved = remove(dataRemovePosition)
        if (wasRemoved) {
            dataChangeListener?.onItemRemoved(removedItem, dataRemovePosition, direction)
        }
    }

    /**
     * Moves item from one position to another.
     * @param fromPosition view start position
     * @param toPosition view end position
     * @return returns true if transition is successful
     */
    internal fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (!isItemMoveAllowed(fromPosition, toPosition)) {
            return false
        }

        val viewType = getItemViewType(toPosition)
        if (viewType == TYPE_HEADER_ITEM || viewType == TYPE_FOOTER_ITEM) {
            return false
        }

        val dataFromPosition = fromPosition + getDataOffset(headerEnabled)
        val dataToPosition = toPosition + getDataOffset(headerEnabled)
        if (swappedItem == null) {
            startDragPos = dataFromPosition
            swappedItem = _data[dataFromPosition]
        }
        stopDragPos = dataToPosition

        // Steps bigger than one we have to swap manually in right order
        val jumpSize = abs(toPosition - fromPosition)
        if (jumpSize > 1) {
            val sign = Integer.signum(toPosition - fromPosition)
            var startPos = dataFromPosition
            for (i in 0 until jumpSize) {
                val endPos = startPos + sign
                Collections.swap(_data, startPos, endPos)
                startPos += sign
            }
        } else {
            Collections.swap(_data, dataFromPosition, dataToPosition)
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

                val revertReorderTransaction = RevertReorderTransaction<T>(startDragPos, stopDragPos, headerEnabled)
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

