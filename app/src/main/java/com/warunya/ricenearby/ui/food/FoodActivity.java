package com.warunya.ricenearby.ui.food;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;

import org.parceler.Parcels;

public class FoodActivity extends AbstractActivity implements FoodContract.View {

    private Food food;

    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvDetail;

    private FoodContract.Presenter presenter = new FoodPresenter(this);

    public static void start(Food food) {
        Intent intent = new Intent(MyApplication.applicationContext, FoodActivity.class);
        intent.putExtra("food", Parcels.wrap(food));
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

        food = Parcels.unwrap(getIntent().getParcelableExtra("food"));

        bindView();
        bindAction();

    }

    private void bindView() {
        tvFoodName = findViewById(R.id.tv_food_name);
        tvPrice = findViewById(R.id.tv_price);
        tvDetail = findViewById(R.id.tv_detail);

        if (food == null) return;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvDetail.setText(food.detail);
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
