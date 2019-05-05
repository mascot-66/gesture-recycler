package com.thesurix.example.gesturerecycler

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.thesurix.example.gesturerecycler.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        with(binding) {
            startGridBtn.setOnClickListener {
                startRecyclerActivity(RecyclerActivity.RecyclerOption.GRID)
            }

            startLinearBtn.setOnClickListener {
                startRecyclerActivity(RecyclerActivity.RecyclerOption.LINEAR)
            }

            startEmptyBtn.setOnClickListener {
                startRecyclerActivity(RecyclerActivity.RecyclerOption.EMPTY)
            }
        }
    }

    private fun startRecyclerActivity(option: RecyclerActivity.RecyclerOption) {
        startActivity(RecyclerActivity.getIntent(this@StartActivity, option))
    }
}
