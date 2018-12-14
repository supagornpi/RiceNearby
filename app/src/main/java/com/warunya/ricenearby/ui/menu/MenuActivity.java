package com.warunya.ricenearby.ui.menu;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodView;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;

public class MenuActivity extends AbstractActivity implements MenuContract.View {

    private RecyclerView recyclerView;

    private MenuContract.Presenter presenter = new MenuPresenter(this);
    private CustomAdapter<Food> adapter;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, MenuActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_menu;
    }

    @Override
    protected void setupView() {
        setTitle(R.string.title_menu);
        showBackButton();
        setMenuRightText(R.string.button_edit);
        bindView();
        bindAction();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodView) itemView).bindAction();
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodView(getApplicationContext());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        setOnclickMenuRight(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodActivity.start();
            }
        });

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNotFound() {

    }

    @Override
    public void hideNotFound() {

    }
}
