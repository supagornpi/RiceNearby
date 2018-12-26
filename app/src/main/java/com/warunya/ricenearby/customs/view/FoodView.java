package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.food.FoodActivity;
import com.warunya.ricenearby.utils.GlideLoader;

public class FoodView extends LinearLayout {

    private Food food;

    private LinearLayout layoutItem;
    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvMeal;
    private TextView tvDate;
    private TextView tvEdit;
    private ImageView ivFood;
    private CheckBox checkBox;

    public FoodView(Context context) {
        super(context);
        init();
    }

    public FoodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_food, this);

        tvFoodName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);
        tvMeal = findViewById(R.id.tv_meal);
        tvDate = findViewById(R.id.tv_date);
        tvEdit = findViewById(R.id.tv_edit);
        ivFood = findViewById(R.id.iv_food);
        checkBox = findViewById(R.id.checkbox);
    }

    public void bind(final Food food) {
        if (food == null) return;
        this.food = food;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvMeal.setText(food.meal == null ? "" : "มื้อ : " + food.meal);
        tvDate.setText(food.date == null ? "" : food.date);

        if (food.uploads != null && food.uploads.get(0) != null) {
            GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);
        } else {
            ivFood.setImageResource(R.drawable.logo);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                food.isSelected = b;
            }
        });
    }

    public void bindAction() {
        layoutItem = findViewById(R.id.layout_item);

        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food == null) return;
                FoodActivity.start(food);
            }
        });

        tvEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodActivity.startInEditMode(food);
            }
        });
    }

    public void setSelected(boolean isSelected) {
        checkBox.setChecked(isSelected);
    }

    public void showCheckBox() {
        checkBox.setVisibility(VISIBLE);
    }
}
