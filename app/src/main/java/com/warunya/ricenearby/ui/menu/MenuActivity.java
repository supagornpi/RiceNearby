package com.warunya.ricenearby.ui.menu;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;

import java.util.List;

public class MenuActivity extends AbstractActivity implements MenuContract.View {

    private RecyclerViewProgress viewProgress;
    private ProgressBar progressBar;
    private TextView tvNotFound;

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

        presenter.start();
    }

    private void bindView() {
        viewProgress = findViewById(R.id.recyclerViewProgress);
        progressBar = findViewById(R.id.progressBar);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodView) itemView).bindAction();
                ((FoodView) itemView).bind((Food) item);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodView(getApplicationContext());
            }
        });
        viewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewProgress.recyclerView.setAdapter(adapter);

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
    public void bindItem(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void showProgress() {
        viewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        viewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        viewProgress.showProgress();
    }

    @Override
    public void hideNotFound() {
        viewProgress.hideProgress();
    }
}
