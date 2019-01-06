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
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.food.FoodActivity;
import com.warunya.ricenearby.utils.GlideLoader;

public class CartView extends LinearLayout {

    private Cart cart;
    private OnButtonClickListener onButtonClickListener;

    private LinearLayout layoutItem;
    private TextView tvFoodName;
    private TextView tvSellerName;
    private TextView tvPrice;
    private TextView tvMeal;
    private TextView tvDate;
    private TextView tvDelete;
    private TextView tvAmount;
    private TextView tvMinus;
    private TextView tvPlus;
    private ImageView ivFood;
    private ImageView ivSellerProfile;

    public CartView(Context context) {
        super(context);
        init();
    }

    public CartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_cart, this);

        ivFood = findViewById(R.id.iv_food);
        ivSellerProfile = findViewById(R.id.iv_seller_profile);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvSellerName = findViewById(R.id.tv_seller_name);
        tvPrice = findViewById(R.id.tv_price);
        tvMeal = findViewById(R.id.tv_meal);
//        tvDate = findViewById(R.id.tv_date);
        tvDelete = findViewById(R.id.tv_delete);
        tvAmount = findViewById(R.id.tv_amount);
        tvMinus = findViewById(R.id.tv_minus);
        tvPlus = findViewById(R.id.tv_plus);
    }

    public void bind(final Cart cart, boolean canChangeAmount) {
        if (cart == null) return;
        this.cart = cart;
        Food food = cart.food;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvMeal.setText(food.meal == null ? "" : "มื้อ : " + food.meal);
        tvAmount.setText(String.valueOf(cart.amount));
//        tvDate.setText(food.date == null ? "" : food.date);

        if (food.uploads != null && food.uploads.get(0) != null) {
            GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);
        } else {
            ivFood.setImageResource(R.drawable.logo);
        }

        tvPlus.setVisibility(canChangeAmount ? VISIBLE : GONE);
        tvMinus.setVisibility(canChangeAmount ? VISIBLE : GONE);

        UserManager.getUserProfile(food.uid, new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                tvSellerName.setText(user.name == null ? user.username : user.name);
                if (user.image != null) {
                    GlideLoader.Companion.loadImageCircle(user.image.url, ivFood);
                } else {
                    ivSellerProfile.setImageResource(R.drawable.logo);
                }
            }
        });
    }

    public void bindAction() {
        layoutItem = findViewById(R.id.layout_item);

        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart == null) return;
                FoodActivity.start(cart.food);
            }
        });

        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonClickListener == null) return;
                onButtonClickListener.onClickedDelete();
            }
        });

        tvPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.amount >= cart.food.amount) {
                    return;
                } else {
                    cart.amount++;
                }
                tvAmount.setText(String.valueOf(cart.amount));
                if (onButtonClickListener == null) return;
                onButtonClickListener.onClickedPlus();
            }
        });

        tvMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.amount <= 1) {
                    return;
                } else {
                    cart.amount--;
                }
                tvAmount.setText(String.valueOf(cart.amount));
                if (onButtonClickListener == null) return;
                onButtonClickListener.onClickedMinus();
            }
        });
    }

    public interface OnButtonClickListener {
        void onClickedPlus();

        void onClickedMinus();

        void onClickedDelete();
    }
}
