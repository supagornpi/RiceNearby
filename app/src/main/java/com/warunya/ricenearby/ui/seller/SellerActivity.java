package com.warunya.ricenearby.ui.seller;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.menu.MenuActivity;

public class SellerActivity extends AbstractActivity implements SellerContract.View {

    private Button btnAddFood;
    private LinearLayout llMenu;

    private SellerContract.Presenter presenter = new SellerPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, SellerActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_seller;
    }

    @Override
    protected void setupView() {
        setTitle("");
        showBackButton();
        setToolbarColor(android.R.color.transparent);
        bindView();
        bindAction();
    }

    private void bindView() {
        btnAddFood = findViewById(R.id.btn_add_food);
        llMenu = findViewById(R.id.layout_menu);
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
