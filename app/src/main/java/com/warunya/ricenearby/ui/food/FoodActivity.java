package com.warunya.ricenearby.ui.food;

import android.content.Intent;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;

public class FoodActivity extends AbstractActivity implements FoodContract.View {

    private FoodContract.Presenter presenter = new FoodPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, FoodActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_food;
    }

    @Override
    protected void setupView() {
        showBackButton();
        bindView();
        bindAction();
    }

    private void bindView() {

    }

    private void bindAction() {

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
