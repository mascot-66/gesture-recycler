package com.thesurix.example.gesturerecycler;

import com.thesurix.example.gesturerecycler.fragment.EmptyViewFragment;
import com.thesurix.example.gesturerecycler.fragment.GridRecyclerFragment;
import com.thesurix.example.gesturerecycler.fragment.LinearRecyclerFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class RecyclerActivity extends AppCompatActivity {

    private static final String EXTRA_RECYCLER_OPTION = "recycler_option";

    public enum RecyclerOption {
        LINEAR, GRID, EMPTY
    }

    public static Intent getIntent(final Context ctx, final RecyclerOption option) {
        final Intent intent = new Intent(ctx, RecyclerActivity.class);
        intent.putExtra(EXTRA_RECYCLER_OPTION, option.ordinal());

        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        final int option = this.getIntent().getIntExtra(EXTRA_RECYCLER_OPTION, RecyclerOption.LINEAR.ordinal());

        final Fragment fragment;
        if (option == RecyclerOption.GRID.ordinal()) {
            fragment = new GridRecyclerFragment();
        } else if (option == RecyclerOption.EMPTY.ordinal()) {
            fragment = new EmptyViewFragment();
        } else {
            fragment = new LinearRecyclerFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_placeholder, fragment).commit();
    }
}
