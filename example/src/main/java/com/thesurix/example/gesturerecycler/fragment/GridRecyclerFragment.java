package com.thesurix.example.gesturerecycler.fragment;

import com.thesurix.example.gesturerecycler.R;
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter;
import com.thesurix.example.gesturerecycler.model.MonthItem;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureListener;
import com.thesurix.gesturerecycler.GestureTouchHelperCallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

public class GridRecyclerFragment extends BaseFragment {

    private GestureTouchHelperCallback mTouchHelperCallback;

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        final MonthsAdapter adapter = new MonthsAdapter(getContext(), R.layout.grid_item);
        adapter.setData(getMonths());
        mRecyclerView.setAdapter(adapter);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int position) {
                final MonthItem item = adapter.getData().get(position);
                return item.getType() == MonthItem.MonthItemType.HEADER ? manager.getSpanCount() : 1;
            }
        });

        mTouchHelperCallback = new GestureTouchHelperCallback(adapter);
        mTouchHelperCallback.setSwipeEnabled(true);
        mTouchHelperCallback.setLongPressDragEnabled(true);
        mTouchHelperCallback.setGestureFlagsForLayout(manager);

        final ItemTouchHelper touchHelper = new ItemTouchHelper(mTouchHelperCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        adapter.setGestureListener(new GestureListener(touchHelper));
        adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<MonthItem>() {
            @Override
            public void onItemRemoved(final MonthItem item, final int position) {
                Snackbar.make(view, "Month removed from position " + position, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReorder(final MonthItem item, final int fromPos, final int toPos) {
                Snackbar.make(view, "Month moved from position " + fromPos + " to " + toPos, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.recycler_drag_menu) {
            mTouchHelperCallback.setManualDragEnabled(!mTouchHelperCallback.isManualDragEnabled());
        }
        return super.onOptionsItemSelected(item);
    }
}
