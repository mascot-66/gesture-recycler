package com.thesurix.example.gesturerecycler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thesurix.example.gesturerecycler.fragment.EmptyViewFragment
import com.thesurix.example.gesturerecycler.fragment.GridRecyclerFragment
import com.thesurix.example.gesturerecycler.fragment.LinearRecyclerFragment

private const val EXTRA_RECYCLER_OPTION = "recycler_option"

class RecyclerActivity : AppCompatActivity() {

    enum class RecyclerOption {
        LINEAR, GRID, EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val option = this.intent.getIntExtra(EXTRA_RECYCLER_OPTION, RecyclerOption.LINEAR.ordinal)
        val fragment = when (option) {
            RecyclerOption.GRID.ordinal -> GridRecyclerFragment()
            RecyclerOption.EMPTY.ordinal -> EmptyViewFragment()
            else -> LinearRecyclerFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_placeholder, fragment).commit()
    }
}

fun getIntent(ctx: Context, option: RecyclerActivity.RecyclerOption): Intent {
    val intent = Intent(ctx, RecyclerActivity::class.java)
    intent.putExtra(EXTRA_RECYCLER_OPTION, option.ordinal)

    return intent
}
