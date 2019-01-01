package com.warunya.ricenearby.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.menu.MenuActivity;
import com.warunya.ricenearby.ui.settime.SetTimeActivity;

public class SellerActivity extends AbstractActivity implements SellerContract.View {

    private Button btnAddFood;
    private LinearLayout llMenu;
    private LinearLayout llSetTime;

    private SellerContract.Presenter presenter = new SellerPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, SellerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_seller;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle("");
        showBackButton();
        setToolbarColor(android.R.color.transparent);
        bindView();
        bindAction();
    }

    private void bindView() {
        btnAddFood = findViewById(R.id.btn_add_food);
        llMenu = findViewById(R.id.layout_menu);
        llSetTime = findViewById(R.id.layout_set_time);
    }

    private void bindAction() {
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodActivity.start();
            }
        });
        llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.start();
            }
        });
        llSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeActivity.start();
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
