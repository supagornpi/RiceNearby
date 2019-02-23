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

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.constant.FoodType;
import com.warunya.ricenearby.ui.home.viewall.ViewAllFoodTypeActivity;
import com.warunya.ricenearby.utils.ResolutionUtils;

public class FoodTypeView extends LinearLayout {

    private LinearLayout layoutItem;
    private TextView tvFoodType;
    private ImageView ivIcon;

    public FoodTypeView(Context context) {
        super(context);
        init();
    }

    public FoodTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodTypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_food_type, this);

        layoutItem = findViewById(R.id.layout_item);
        tvFoodType = findViewById(R.id.tv_food_type);

        ivIcon = findViewById(R.id.iv_icon);

        ResolutionUtils.setViewSize(getContext(), 3.0f, 3.0f, layoutItem);
    }

    public void bind(final FoodType foodType, int position) {
        tvFoodType.setText(foodType.getFoodTypeName());
        ivIcon.setImageResource(foodType.getIconId());

        layoutItem.setBackgroundResource(position % 2 == 0
                ? R.drawable.background_color_orange : R.drawable.background_color_yellow_light);
    }

    public void bindAction(final FoodType foodType) {
        layoutItem = findViewById(R.id.layout_item);

        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAllFoodTypeActivity.start(foodType.getFoodTypeName(), foodType.ordinal());
            }
        });
    }
}
