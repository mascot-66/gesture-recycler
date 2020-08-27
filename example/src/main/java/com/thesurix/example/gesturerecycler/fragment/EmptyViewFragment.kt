package com.thesurix.example.gesturerecycler.fragment

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter
import com.thesurix.example.gesturerecycler.callback.MonthDiffCallback
import com.thesurix.example.gesturerecycler.model.Month
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.EmptyViewVisibilityListener
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureManager

class EmptyViewFragment : BaseFragment() {

    private var adapter: MonthsAdapter? = null

    private var headerFooterState = false
    override val months: MutableList<MonthItem>
        get() {
            return mutableListOf(Month("JAN", R.drawable.january),
                    Month("FEB", R.drawable.february),
                    Month("MAR", R.drawable.march),
                    Month("APR", R.drawable.april),
                    Month("MAY", R.drawable.may),
                    Month("JUN", R.drawable.june))
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recycler, container, false)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        adapter = MonthsAdapter(R.layout.linear_item_with_background).apply {
            data = months
            setUndoSize(2)
            setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
                override fun onItemRemoved(item: MonthItem, position: Int, direction: Int) {
                    val undoSnack = Snackbar.make(view, "Month removed from position $position", Snackbar.LENGTH_SHORT)
                    undoSnack.setAction(R.string.undo_text) { adapter?.undoLast() }
                    undoSnack.show()
                }

                override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
                    val undoSnack = Snackbar.make(view, "Month moved from position $fromPos to $toPos", Snackbar.LENGTH_SHORT)
                    undoSnack.setAction(R.string.undo_text) { adapter?.undoLast() }
                    undoSnack.show()
                }
            })

            val emptyView = view.findViewById<View>(R.id.empty_root)
            setEmptyViewVisibilityListener(object : EmptyViewVisibilityListener {
                override fun onVisibilityChanged(visible: Boolean) {
                    if (visible) {
                        emptyView.visibility = View.VISIBLE
                        runFadeInAnimation(emptyView)
                    } else {
                        runFadeOutAnimation(emptyView)
                    }
                }
            })
        }

        recyclerView.adapter = adapter

        gestureManager = GestureManager.Builder(recyclerView)
                .setSwipeEnabled(true)
                .setSwipeFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                .setLongPressDragEnabled(true)
                .setHeaderEnabled(headerFooterState)
                .setFooterEnabled(headerFooterState)
                .build()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recycler_empty_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.recycler_undo_menu -> adapter?.undoLast()
            R.id.recycler_clear_menu -> adapter?.clearData()
            R.id.recycler_generate_menu -> {
                val months = months
                val month = (Math.random() * months.size).toInt()
                adapter?.insert(months[month], 0)
                recyclerView.scrollToPosition(0)
            }
            R.id.recycler_diff_menu -> {
                val diffMonths = months
                diffMonths.shuffle()
                adapter?.setData(diffMonths, MonthDiffCallback(adapter!!.data, diffMonths))
                recyclerView.scrollToPosition(0)
            }
            R.id.add -> {
                val months = months
                val month = (Math.random() * months.size).toInt()
                adapter?.add(months[month])
            }
            R.id.toggle_hf -> {
                headerFooterState = !headerFooterState
                adapter?.setHeaderEnabled(headerFooterState)
                adapter?.setFooterEnabled(headerFooterState)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun runFadeInAnimation(emptyView: View) {
        val fadeInAnimation = ValueAnimator.ofObject(FloatEvaluator(), 0f, 1f)
        fadeInAnimation.duration = resources.getInteger(R.integer.animation_speed_ms).toLong()
        fadeInAnimation.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            emptyView.alpha = alpha
        }
        fadeInAnimation.start()
    }

    private fun runFadeOutAnimation(emptyView: View) {
        val fadeOutAnimation = ValueAnimator.ofObject(FloatEvaluator(), 1f, 0f)
        fadeOutAnimation.duration = resources.getInteger(R.integer.animation_speed_ms).toLong()
        fadeOutAnimation.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            emptyView.alpha = alpha

            if (alpha < 0.01f) {
                emptyView.visibility = View.GONE
            }
        }

        fadeOutAnimation.start()
    }
}
