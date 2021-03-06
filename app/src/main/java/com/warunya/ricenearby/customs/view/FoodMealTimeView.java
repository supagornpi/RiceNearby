package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.utils.ConvertDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FoodMealTimeView extends LinearLayout {

    private Meal meal;

    private TextView tvDate;
    private TextView tvDelete;
    private TextView tvAmount;

    public FoodMealTimeView(Context context) {
        super(context);
        init();
    }

    public FoodMealTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodMealTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodMealTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_food_meal_time, this);
        getRootView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvDate = findViewById(R.id.tv_date);
        tvDelete = findViewById(R.id.tv_delete);
        tvAmount = findViewById(R.id.tv_amount);
    }

    public void bind(final Meal meal) {
        if (meal == null) return;
        this.meal = meal;
        tvDate.setText(meal.date);
        tvAmount.setText("จำนวน " + meal.amount + " จาน");

    }

    public void bindInCart(final Meal meal) {
        if (meal == null) return;
        this.meal = meal;
        tvDate.setText(meal.date);
        tvAmount.setText("จำนวน " + meal.amount + " จาน");

        tvDate.setText(ConvertDateUtils.getNewDateFormatFOrMealTime(meal.date) + " " + meal.mealTime.getMealTimeText());
    }

    public void bindAction(final OnChildButtonClickListener onChildButtonClickListener) {
        tvDelete.setVisibility(VISIBLE);
        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meal == null) return;
                if (onChildButtonClickListener == null) return;
                onChildButtonClickListener.onClickedDeleteChild(meal.key);
            }
        });
    }

    public interface OnChildButtonClickListener {
        void onClickedDeleteChild(String childKey);
    }
}
