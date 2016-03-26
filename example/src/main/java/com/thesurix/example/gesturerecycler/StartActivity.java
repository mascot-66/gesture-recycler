package com.thesurix.example.gesturerecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_grid_btn)
    public void startGridRecycler() {
        startActivity(RecyclerActivity.getIntent(this, RecyclerActivity.RecyclerOption.GRID));
    }

    @OnClick(R.id.start_linear_btn)
    public void startLinearRecycler() {
        startActivity(RecyclerActivity.getIntent(this, RecyclerActivity.RecyclerOption.LINEAR));
    }

    @OnClick(R.id.start_empty_btn)
    public void startLinearEmptyRecycler() {
        startActivity(RecyclerActivity.getIntent(this, RecyclerActivity.RecyclerOption.EMPTY));
    }
}
