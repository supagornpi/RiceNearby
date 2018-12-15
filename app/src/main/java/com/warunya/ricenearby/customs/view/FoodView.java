package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.food.FoodActivity;

public class FoodView extends LinearLayout {

    private Food food;

    private LinearLayout layoutItem;
    private TextView tvFoodName;
    private TextView tvPrice;

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
    }

    public void bind(Food food) {
       this.food = food;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
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
    }
}
