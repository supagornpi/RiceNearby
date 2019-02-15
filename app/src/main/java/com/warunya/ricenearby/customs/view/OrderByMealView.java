package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.MealByDate;

public class OrderByMealView extends LinearLayout {

    private MealByDate mealByDate;
    private boolean isMyOrder = false;
    private CustomAdapter<Food> adapter;

    private LinearLayout layoutItem;
    private TextView tvMealTime;
    private RecyclerViewProgress recyclerViewProgress;

    public OrderByMealView(Context context) {
        super(context);
        init();
    }

    public OrderByMealView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderByMealView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OrderByMealView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_order_by_meal, this);
        getRootView().setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvMealTime = findViewById(R.id.tv_meal_time);
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress_food);
    }

    public void bind(final MealByDate mealByDate) {
        if (mealByDate == null) return;
        this.mealByDate = mealByDate;

        tvMealTime.setText(mealByDate.mealTime.getMealTimeText());

        initRecyclerView();
        if (mealByDate.foodList != null) {
            adapter.setListItem(mealByDate.foodList);
        }
    }

    private void initRecyclerView() {
        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                final Food food = ((Food) item);
                if (food == null) return;
                ((FoodSummaryView) itemView).bind(food, mealByDate.meal.amount);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodSummaryView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setAdapter(adapter);
    }

    public void bindAction() {
        layoutItem = findViewById(R.id.layout_item);

//        layoutItem.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerViewProgress.setVisibility(recyclerViewProgress.isShown() ? GONE : VISIBLE);
//            }
//        });

    }


}
