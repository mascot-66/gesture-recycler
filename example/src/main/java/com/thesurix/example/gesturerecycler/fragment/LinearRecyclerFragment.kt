package com.thesurix.example.gesturerecycler.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.DefaultItemClickListener
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureManager
import com.thesurix.gesturerecycler.RecyclerItemTouchListener

class LinearRecyclerFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        val adapter = MonthsAdapter(R.layout.linear_item)
        adapter.data = months

        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(RecyclerItemTouchListener(object : DefaultItemClickListener<MonthItem>() {

            override fun onItemClick(item: MonthItem, position: Int): Boolean {
                Snackbar.make(view, "Click event on the $position position", Snackbar.LENGTH_SHORT).show()
                return true
            }

            override fun onItemLongPress(item: MonthItem, position: Int) {
                Snackbar.make(view, "Long press event on the $position position", Snackbar.LENGTH_SHORT).show()
            }

            override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
                Snackbar.make(view, "Double tap event on the $position position", Snackbar.LENGTH_SHORT).show()
                return true
            }
        }))

        gestureManager = GestureManager.Builder(recyclerView)
                .setSwipeEnabled(true)
                .setLongPressDragEnabled(true)
                .setSwipeFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                .setDragFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
                .build()

        adapter.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            override fun onItemRemoved(item: MonthItem, position: Int, direction: Int) {
                Snackbar.make(view, "Month removed from position $position ", Snackbar.LENGTH_SHORT).show()
            }

            override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
                Snackbar.make(view, "Month moved from position $fromPos to $toPos", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.recycler_drag_menu) {
            gestureManager?.isManualDragEnabled = !gestureManager!!.isManualDragEnabled
        }
        return super.onOptionsItemSelected(item)
    }
}
