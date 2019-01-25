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
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.model.Meal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MealTimeView extends LinearLayout {

    private Meal meal;

    private TextView tvDate;
    private TextView tvAmount;
    private LinearLayout layoutItem;

    public MealTimeView(Context context) {
        super(context);
        init();
    }

    public MealTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MealTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MealTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_meal_time, this);

        tvDate = findViewById(R.id.tv_meal_time);
        tvAmount = findViewById(R.id.tv_amount);
        layoutItem = findViewById(R.id.layout_item);
    }

    public void bind(final Meal meal) {
        if (meal == null) return;
        this.meal = meal;
        tvDate.setText(getNewDateFormat(meal.date) + " " + meal.mealTime.getMealTimeText());
//        tvAmount.setText(meal.amount + " จาน");
    }

    public void bindAction(final OnItemClickListener onItemClickListener) {
        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClicked();
            }
        });
    }

    public void setSelected(boolean isSelected) {
        layoutItem.setSelected(isSelected);
    }

    private String getNewDateFormat(String date) {
        String myFormat = "EE dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        try {
            Date newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("dd MMM");
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public interface OnItemClickListener {
        void onClicked();
    }
}
