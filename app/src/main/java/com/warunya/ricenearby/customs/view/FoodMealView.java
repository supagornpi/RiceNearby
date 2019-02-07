package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.utils.GlideLoader;

public class FoodMealView extends LinearLayout {

    private Food food;

    private TextView tvFoodName;
    private TextView tvDelete;
    private TextView tvAddDate;
    private ImageView ivFood;
    private RecyclerView recyclerViewChild;

    public FoodMealView(Context context) {
        super(context);
        init();
    }

    public FoodMealView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodMealView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodMealView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_food_meal, this);
        getRootView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvFoodName = findViewById(R.id.tv_food_name);
        tvDelete = findViewById(R.id.tv_delete);
        tvAddDate = findViewById(R.id.tv_add_date);
        ivFood = findViewById(R.id.iv_food);
        recyclerViewChild = findViewById(R.id.recyclerViewChild);
    }

    public void bind(final Food food, MealTime mealTime, final OnButtonClickListener onButtonClickListener) {
        if (food == null) return;
        this.food = food;

        tvFoodName.setText(food.foodName);

        if (food.uploads != null && food.uploads.get(0) != null) {
            GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);
        } else {
            ivFood.setImageResource(R.drawable.logo);
        }

        generateChild(food, mealTime, onButtonClickListener);
        bindAction(onButtonClickListener);

    }

    public void bindAction(final OnButtonClickListener onButtonClickListener) {
        tvAddDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonClickListener == null) return;
                onButtonClickListener.onClickedAddDate(food);
            }
        });

        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food == null) return;
                if (onButtonClickListener == null) return;
                onButtonClickListener.onClickedDelete(food.key);
            }
        });
    }

    private void generateChild(final Food food, final MealTime mealTime, final OnButtonClickListener onChildButtonClickListener) {
        //init adapter
        CustomAdapter<Meal> adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodMealTimeView) itemView).bind((Meal) item);
                ((FoodMealTimeView) itemView).bindAction(new FoodMealTimeView.OnChildButtonClickListener() {
                    @Override
                    public void onClickedDeleteChild(String childKey) {
                        if (onChildButtonClickListener == null) return;
                        onChildButtonClickListener.onClickedDeleteChild(food.key, childKey);
                    }
                });
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodMealTimeView(parent.getContext());
            }
        });
        recyclerViewChild.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChild.setAdapter(adapter);

        //set items
        if (mealTime == MealTime.Breakfast) {
            adapter.setListItem(food.breakfasts);
        } else if (mealTime == MealTime.Lunch) {
            adapter.setListItem(food.lunches);
        } else if (mealTime == MealTime.Dinner) {
            adapter.setListItem(food.dinners);
        }
    }

    public interface OnButtonClickListener {
        void onClickedAddDate(Food food);

        void onClickedDelete(String key);

        void onClickedDeleteChild(String key, String childKey);
    }
}
