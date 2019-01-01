package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.warunya.ricenearby.R;

public class RecyclerViewProgress extends LinearLayout {

    public RecyclerView recyclerView = findViewById(R.id.recyclerView);
    private TextView tvNotFound;
    private ProgressBar progressBar;

    public RecyclerViewProgress(Context context) {
        super(context);
        init();
    }

    public RecyclerViewProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerViewProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerViewProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_recyclerview_with_progress, this);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvNotFound = findViewById(R.id.tvNotFound);
    }

    public void showProgress() {
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(GONE);
    }

    public void showNotFound() {
        recyclerView.setVisibility(GONE);
        tvNotFound.setVisibility(VISIBLE);
    }

    public void hideNotFound() {
        recyclerView.setVisibility(VISIBLE);
        tvNotFound.setVisibility(GONE);
    }
}
