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
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;

import org.jetbrains.annotations.Nullable;
import org.parceler.Parcels;

import java.util.List;

public class ViewAllFoodActivity extends AbstractActivity {

    private CustomAdapter<Food> adapter;
    private RecyclerViewProgress recyclerViewProgress;

    public static void start(String title, List<Food> foods) {
        Intent intent = new Intent(MyApplication.applicationContext, ViewAllFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("foods", Parcels.wrap(foods));
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
        List<Food> foods = Parcels.unwrap(getIntent().getParcelableExtra("foods"));

        String title = getIntent().getStringExtra("title");
        if (title.equals("อาหารแนะนำ")) {
            adapter.setMaximumItem(10);
        }

        adapter.setListItem(foods);

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
}
