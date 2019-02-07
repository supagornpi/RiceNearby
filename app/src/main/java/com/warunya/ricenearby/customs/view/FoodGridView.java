package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.food.FoodActivity;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;

public class FoodGridView extends LinearLayout {

    private Food food;

    private LinearLayout layoutItem;
    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvSellerName;
    private TextView tvDistance;
    //    private TextView tvDate;
//    private TextView tvEdit;
    private ImageView ivFood;

    public FoodGridView(Context context) {
        super(context);
        init();
    }

    public FoodGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_food_grid, this);

        tvFoodName = findViewById(R.id.tv_food_name);
        tvPrice = findViewById(R.id.tv_price);
        tvSellerName = findViewById(R.id.tv_seller_name);
        tvDistance = findViewById(R.id.tv_distance);
//        tvDate = findViewById(R.id.tv_date);
//        tvEdit = findViewById(R.id.tv_edit);
        ivFood = findViewById(R.id.iv_food);
        layoutItem = findViewById(R.id.layout_item);

    }

    public void bind(final Food food) {
        if (food == null) return;
        this.food = food;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");

        if (food.sellerName == null || food.sellerName.isEmpty()) {
            UserManager.getUserProfileSingleValueEvent(food.uid, new UserManager.OnValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) return;
                    food.sellerName = user.name != null ? user.name : user.username;
                    tvSellerName.setText(food.sellerName);
                }
            });
        } else {
            tvSellerName.setText(food.sellerName);
        }

        if (food.distance != null) {
            String distance = food.distance >= 1000 ? food.distance.intValue() / 1000 + "km" : food.distance.intValue() + "m";
            tvDistance.setText(distance);
        }

        if (food.uploads != null && food.uploads.get(0) != null) {
            GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);
        } else {
            ivFood.setImageResource(R.drawable.logo);
        }

    }

    public void setHorizontalItem() {
        ResolutionUtils.setViewWidth(getContext(), 2.3f, layoutItem);
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
