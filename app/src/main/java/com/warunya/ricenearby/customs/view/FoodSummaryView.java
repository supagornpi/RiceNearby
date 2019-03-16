package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.utils.GlideLoader;

public class FoodSummaryView extends LinearLayout {

    private Food food;

    private TextView tvFoodName;
    private TextView tvAmount;
    private ImageView ivFood;

    public FoodSummaryView(Context context) {
        super(context);
        init();
    }

    public FoodSummaryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodSummaryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodSummaryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_food_summary, this);
        getRootView().setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvFoodName = findViewById(R.id.tv_name);
        tvAmount = findViewById(R.id.tv_amount);
        ivFood = findViewById(R.id.iv_food);
    }

    public void bind(final Food food, int amount) {
        if (food == null) return;
        this.food = food;
        tvFoodName.setText(food.foodName);
        tvAmount.setText("จำนวน " + amount + " จาน");

        if (food.uploads != null && food.uploads.get(0) != null) {
            GlideLoader.load(food.uploads.get(0).url, ivFood);
        } else {
            ivFood.setImageResource(R.drawable.logo);
        }
    }
}
