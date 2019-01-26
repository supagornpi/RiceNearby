package com.warunya.ricenearby.ui.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.SimplePagerAdapter;
import com.warunya.ricenearby.dialog.AddCartDialog;
import com.warunya.ricenearby.dialog.ImageBitmapDialog;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.utils.BitmapUtils;
import com.warunya.ricenearby.utils.ConvertDateUtils;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class FoodActivity extends AbstractActivity implements FoodContract.View {

    private Food food;

    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvDetail;
    private TextView tvMealTime;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView btnAddCart;
    private TextView btnBuy;
    private LinearLayout layoutBtnBuy;

    private FoodContract.Presenter presenter = new FoodPresenter(this);

    public static void start(Food food) {
        Intent intent = new Intent(MyApplication.applicationContext, FoodActivity.class);
        intent.putExtra("food", Parcels.wrap(food));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_food;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        showBackButton();

        food = Parcels.unwrap(getIntent().getParcelableExtra("food"));

        bindView();
        bindAction();

        //cannot buy food of self
        layoutBtnBuy.setVisibility(food.uid.equals(UserManager.getUid()) ? View.GONE : View.VISIBLE);

        SimplePagerAdapter<Upload> adapter = new SimplePagerAdapter<Upload>().setOnInflateViewListener(new SimplePagerAdapter.OnInflateViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int position) {
                ImageView ivFood = itemView.findViewById(R.id.iv_food);
//                set banner height
                ivFood.getLayoutParams().height = ResolutionUtils.getBannerHeightFromRatio(itemView.getContext());

                if (((Upload) item) == null) return;
                GlideLoader.Companion.load(((Upload) item).url, ivFood);
            }

            @Override
            public int getLayout() {
                return R.layout.item_food_image;
            }
        });

        adapter.setListItems(food.uploads);

        //set product image list
        viewPager.setAdapter(adapter);
        circleIndicator.removeAllViews();
        if (adapter.getCount() > 1) {
            circleIndicator.setViewPager(viewPager);
        }
    }

    private void bindView() {
        tvFoodName = findViewById(R.id.tv_food_name);
        tvPrice = findViewById(R.id.tv_price);
        tvDetail = findViewById(R.id.tv_detail);
        tvMealTime = findViewById(R.id.tv_meal_time);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleindicator);
        btnAddCart = findViewById(R.id.btn_add_cart);
        btnBuy = findViewById(R.id.btn_buy);
        layoutBtnBuy = findViewById(R.id.layout_buy);

        if (food == null) return;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvDetail.setText(food.detail);

        //Meal time
        List<Meal> meals = new ArrayList<>();
        if (food.breakfasts != null) {
            meals.addAll(food.breakfasts);
        }
        if (food.lunches != null) {
            meals.addAll(food.lunches);
        }
        if (food.dinners != null) {
            meals.addAll(food.dinners);
        }

        if (meals.size() != 0) {
            StringBuilder allMeal = new StringBuilder();
            for (Meal meal : meals) {
                allMeal.append(ConvertDateUtils.getNewDateFormatFOrMealTime(meal.date))
                        .append(" ")
                        .append(meal.mealTime.getMealTimeText())
                        .append("     ");
            }
            tvMealTime.setText(allMeal.toString());
        }

    }

    private void bindAction() {
        setOnclickMenuRight(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodActivity.start();
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (food == null) return;
                AddCartDialog.show(FoodActivity.this, food, new AddCartDialog.OnClickListener() {
                    @Override
                    public void onClickedAddToCart(int amount, Meal meal) {
                        presenter.addToCart(food, amount, meal);
                        //play animate add to cart
                        Bitmap bitmap = BitmapUtils.getBitmapFromView(viewPager);
                        ImageBitmapDialog imageBitmapDialog = new ImageBitmapDialog(FoodActivity.this, bitmap);
                        imageBitmapDialog.show();
                    }
                });
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    @Override
    public void addCartSuccess() {

    }
}
