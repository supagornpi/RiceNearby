package com.warunya.ricenearby.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.utils.GlideLoader;

public class AddCartDialog extends Dialog {

    private int amount = 1;
    private Food food;
    private OnClickListener onClickListener;

    private ImageButton btnClose;
    private TextView tvPlus;
    private TextView tvMinus;
    private TextView tvAmount;
    private TextView tvPrice;
    private TextView tvFoodName;
    private TextView tvMeal;
    private TextView tvTotalAmount;
    private ImageView ivFood;
    private Button btnAddtoCart;

    public AddCartDialog(@NonNull Context context) {
        super(context);
    }

    public AddCartDialog(@NonNull Context context, Food food) {
        super(context);
        this.food = food;
    }

    public static void show(Context context, Food food, OnClickListener onclickListener) {
        AddCartDialog addCartDialog = new AddCartDialog(context, food).setOnclickListener(onclickListener);
        addCartDialog.show();
    }

    public AddCartDialog setOnclickListener(OnClickListener onclickListener) {
        this.onClickListener = onclickListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_cart);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        bindView();
        bindAction();
    }

    private void bindView() {
        btnClose = findViewById(R.id.btn_close);
        tvPlus = findViewById(R.id.tv_plus);
        tvMinus = findViewById(R.id.tv_minus);
        tvAmount = findViewById(R.id.tv_amount);
        tvPrice = findViewById(R.id.tv_price);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvMeal = findViewById(R.id.tv_meal);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        ivFood = findViewById(R.id.iv_food);
        btnAddtoCart = findViewById(R.id.btn_add_cart);

        tvAmount.setText(String.valueOf(amount));
        if (food == null) return;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvMeal.setText(food.meal);
        tvTotalAmount.setText(getContext().getString(R.string.cart_total_amount, food.amount));

        if (food.uploads == null) return;
        GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);

    }

    private void bindAction() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount >= food.amount) {
                    return;
                } else {
                    amount++;
                }
                tvAmount.setText(String.valueOf(amount));
            }
        });

        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount <= 1) {
                    return;
                } else {
                    amount--;
                }
                tvAmount.setText(String.valueOf(amount));
            }
        });

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener == null) return;
                onClickListener.onClickedAddToCart(amount);
                dismiss();
            }
        });
    }

    public interface OnClickListener {
        void onClickedAddToCart(int amount);
    }
}
