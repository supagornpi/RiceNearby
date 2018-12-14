package com.warunya.ricenearby.ui.food;

import android.content.Intent;
import android.view.View;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;

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
        setMenuRightText(R.string.button_edit);
        bindView();
        bindAction();
    }

    private void bindView() {

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
