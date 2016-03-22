package com.thesurix.gesturerecycler;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base adapter for gesture recognition, extends this to provide own implementation. T is the data type, K is the ViewHolder type.
 * @author thesurix
 */
public abstract class GestureAdapter<T, K extends GestureViewHolder> extends RecyclerView.Adapter<K> {

    /** Listener for data changes inside adapter */
    public interface OnDataChangeListener<T> {

        /**
         * Called when item has been removed.
         * @param item removed item
         * @param position removed position
         */
        void onItemRemoved(T item, int position);

        /**
         * Called when item has been reordered.
         * @param item reordered item
         * @param fromPos reorder start position
         * @param toPos reorder end position
         */
        void onItemReorder(T item, int fromPos, int toPos);
    }

    /** Listener for gestures */
    public interface OnGestureListener {

        /**
         * Called when view holder item has pending drag gesture.
         * @param viewHolder dragged view holder item
         */
        void onStartDrag(GestureViewHolder viewHolder);
    }

    private static final int INVALID_DRAG_POS = -1;

    /** Temp item for swap action */
    private T mSwappedItem;
    /** Start position of the drag action */
    private int mStartDragPos;
    /** Stop position of the drag action */
    private int mStopDragPos = INVALID_DRAG_POS;
    /** Flag that defines if adapter allows manual dragging */
    private boolean mIsManualDragAllowed;

    private OnGestureListener mGestureListener;
    private OnDataChangeListener<T> mDataChangeListener;

    /** Collection for adapter's data */
    private final  List<T> mData = new ArrayList<>();

    @Override
    public void onBindViewHolder(final K holder, final int position) {
        if (holder.getDraggableView() != null) {
            if (mIsManualDragAllowed && holder.canDrag()) {
                holder.showDraggableView();
                holder.getDraggableView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(final View view, final MotionEvent motionEvent) {
                        if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                            if (mGestureListener != null) {
                                mGestureListener.onStartDrag(holder);
                            }
                        }

                        return false;
                    }
                });
            } else {
                holder.hideDraggableView();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Sets adapter data. This method will interrupt pending animations.
     * Use add(), remove() or insert() to achieve smooth animations.
     * @param data data to show
     */
    public void setData(final List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Returns adapter's data.
     * @return adapter's data
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * Adds item to the adapter.
     * @param item item to add
     * @return true if added, false otherwise
     */
    public boolean add(final T item) {
        final boolean added = mData.add(item);
        if (added) {
            notifyItemInserted(mData.size());
        }

        return added;
    }

    /**
     * Removes item from the given position.
     * @param position item's position
     * @return true if removed, false otherwise
     */
    public boolean remove(final int position) {
        final T item = mData.remove(position);
        if (item != null) {
            notifyItemRemoved(position);
            return true;
        }

        return false;
    }

    /**
     * Inserts item in the given position.
     * @param item item to insert
     * @param position position for the item
     */
    public void insert(final T item, final int position) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * Sets adapter gesture listener.
     * @param listener gesture listener
     */
    public void setGestureListener(final OnGestureListener listener) {
        mGestureListener = listener;
    }

    /**
     * Sets adapter data change listener.
     * @param listener data change listener
     */
    public void setDataChangeListener(final OnDataChangeListener<T> listener) {
        mDataChangeListener = listener;
    }

    /**
     * Dismisses item from the given position.
     * @param position item's position
     */
    void onItemDismissed(final int position) {
        final T removed = mData.get(position);
        final boolean wasRemoved = remove(position);
        if (wasRemoved && mDataChangeListener != null) {
            mDataChangeListener.onItemRemoved(removed, position);
        }
    }

    /**
     * Moves item from one position to another.
     * @param fromPos start position
     * @param toPos end position
     * @return returns true if transition is successful
     */
    boolean onItemMove(final int fromPos, final int toPos) {
        if (mSwappedItem == null) {
            mStartDragPos = fromPos;
            mSwappedItem = mData.get(fromPos);
        }
        mStopDragPos = toPos;

        // Steps bigger than one we have to swap manually in right order
        final int jumpSize = Math.abs(toPos - fromPos);
        if (jumpSize > 1) {
            final int sign = Integer.signum(toPos - fromPos);
            int startPos = fromPos;
            for (int i = 0; i < jumpSize; i++) {
                final int endPos = startPos + sign;
                Collections.swap(mData, startPos, endPos);
                startPos += sign;
            }
        } else {
            Collections.swap(mData, fromPos, toPos);
        }
        notifyItemMoved(fromPos, toPos);
        return true;
    }

    /**
     * Called when item has been moved.
     */
    void onItemMoved() {
        if (mSwappedItem != null && mStopDragPos != INVALID_DRAG_POS) {
            mDataChangeListener.onItemReorder(mSwappedItem, mStartDragPos, mStopDragPos);
            mSwappedItem = null;
            mStopDragPos = INVALID_DRAG_POS;
        }
    }

    /**
     * Enables or disables manual drag actions on items. Manual dragging is disabled by default.
     * To allow manual drags provide draggable view, see {@link GestureViewHolder}.
     * @param allowState true to enable, false to disable
     */
    void allowManualDrag(final boolean allowState) {
        mIsManualDragAllowed = allowState;
        notifyDataSetChanged();
    }
}
