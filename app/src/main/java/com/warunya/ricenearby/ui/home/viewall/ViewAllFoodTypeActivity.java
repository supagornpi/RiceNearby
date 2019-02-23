package com.warunya.ricenearby.ui.home.viewall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.FoodType;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ViewAllFoodTypeActivity extends AbstractActivity implements ViewAllContract.View {

    private ViewAllContract.Presenter presenter;

    private CustomAdapter<Food> adapter;
    private RecyclerViewProgress recyclerViewProgress;

    public static void start(String title, int foodType) {
        Intent intent = new Intent(MyApplication.applicationContext, ViewAllFoodTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("foodType", foodType);
        intent.putExtra("title", title);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_view_all_food;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        showBackButton();
        setTitle(getIntent().getStringExtra("title"));
        bindView();
        initRecyclerViewFood();
        int foodType = getIntent().getIntExtra("foodType", 0);
        presenter = new ViewAllPresenter(this, FoodType.Companion.parse(foodType));

        presenter.start();
    }

    private void bindView() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
    }

    private void initRecyclerViewFood() {
        recyclerViewProgress.recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodGridView) itemView).bind(((Food) item));
                ((FoodGridView) itemView).bindAction();

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodGridView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setAdapter(adapter);
    }

    @Override
    public void fetchFoods(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void showProgress() {
        recyclerViewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        recyclerViewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        recyclerViewProgress.setTextNotFound("ไม่มีอาหารประเภทนี้");
        recyclerViewProgress.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
    }
}
