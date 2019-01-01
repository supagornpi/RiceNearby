package com.warunya.ricenearby.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.SimplePagerAdapter;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;

import org.parceler.Parcels;

import me.relex.circleindicator.CircleIndicator;

public class FoodActivity extends AbstractActivity implements FoodContract.View {

    private Food food;

    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvDetail;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

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

        SimplePagerAdapter<Upload> adapter = new SimplePagerAdapter<Upload>().setOnInflateViewListener(new SimplePagerAdapter.OnInflateViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int position) {
                ImageView ivFood = itemView.findViewById(R.id.iv_food);
//                set banner height
                ivFood.getLayoutParams().height = ResolutionUtils.getBannerHeightFromRatio(itemView.getContext());
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
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleindicator);

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
