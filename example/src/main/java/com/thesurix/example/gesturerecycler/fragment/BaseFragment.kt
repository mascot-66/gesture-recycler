package com.thesurix.example.gesturerecycler.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.model.Month
import com.thesurix.example.gesturerecycler.model.MonthHeader
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.GestureManager

open class BaseFragment : Fragment() {

    protected lateinit var recyclerView: RecyclerView
    protected var gestureManager: GestureManager? = null

    protected open val months: MutableList<MonthItem>
        get() {
            return mutableListOf(MonthHeader("First quarter"),
                    Month("JAN", R.drawable.january),
                    Month("FEB", R.drawable.february),
                    Month("MAR", R.drawable.march),
                    MonthHeader("Second quarter"),
                    Month("APR", R.drawable.april),
                    Month("MAY", R.drawable.may),
                    Month("JUN", R.drawable.june),
                    MonthHeader("Third quarter"),
                    Month("JUL", R.drawable.july),
                    Month("AUG", R.drawable.august),
                    Month("SEP", R.drawable.september),
                    MonthHeader("Fourth quarter"),
                    Month("OCT", R.drawable.october),
                    Month("NOV", R.drawable.november),
                    Month("DEC", R.drawable.december))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = RecyclerView(activity!!)
        return recyclerView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.recycler_menu, menu)
    }
}
