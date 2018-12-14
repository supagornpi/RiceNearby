package com.warunya.ricenearby.ui.addfood;

import android.content.Intent;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;

public class AddFoodActivity extends AbstractActivity implements AddFoodContract.View {

    private AddFoodContract.Presenter presenter = new AddFoodPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, AddFoodActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_add_food;
    }

    @Override
    protected void setupView() {
        setTitle(R.string.title_add_food);
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
