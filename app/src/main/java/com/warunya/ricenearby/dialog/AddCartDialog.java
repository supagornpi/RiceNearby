package com.warunya.ricenearby.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.ui.food.MealTimeAdapter;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

public class AddCartDialog extends Dialog {

    private int amount = 1;
    private int totalAmount = 0;
    private boolean isBuyNow = false;
    private Meal currentSelectedMeal;
    private Food food;
    private OnClickListener onClickListener;

    private ImageButton btnClose;
    private TextView tvPlus;
    private TextView tvMinus;
    private EditText edtAmount;
    private TextView tvPrice;
    private TextView tvFoodName;
    private TextView tvMeal;
    private TextView tvTotalAmount;
    private ImageView ivFood;
    private Button btnAddtoCart;
    private RecyclerView recyclerView;

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

    public static void showBuyNow(Context context, Food food, OnClickListener onclickListener) {
        AddCartDialog addCartDialog = new AddCartDialog(context, food)
                .setOnclickListener(onclickListener)
                .setBuyNow();
        addCartDialog.show();
    }

    public AddCartDialog setOnclickListener(OnClickListener onclickListener) {
        this.onClickListener = onclickListener;
        return this;
    }

    public AddCartDialog setBuyNow() {
        isBuyNow = true;
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
        edtAmount = findViewById(R.id.edt_amount);
        tvPrice = findViewById(R.id.tv_price);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvMeal = findViewById(R.id.tv_meal);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        ivFood = findViewById(R.id.iv_food);
        btnAddtoCart = findViewById(R.id.btn_add_cart);
        recyclerView = findViewById(R.id.recyclerView);

        edtAmount.setText(String.valueOf(amount));

        addTextChangeListener();

        if (food == null) return;
        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvMeal.setText(food.meal);
        tvTotalAmount.setText(getContext().getString(R.string.cart_total_amount, food.amount));

        bindAdapter();

        if (food.uploads != null) {
            GlideLoader.Companion.load(food.uploads.get(0).url, ivFood);
        }

        if (isBuyNow) {
            btnAddtoCart.setText(getContext().getResources().getString(R.string.button_buy_now));
        }

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
                if (amount >= totalAmount) {
                    return;
                } else {
                    amount++;
                }
                edtAmount.setText(String.valueOf(amount));
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
                edtAmount.setText(String.valueOf(amount));
            }
        });

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener == null) return;
                if (amount == 0) return;
                onClickListener.onClickedAddToCart(amount, currentSelectedMeal);
                dismiss();
            }
        });
    }

    private void bindAdapter() {
        List<Meal> meals = new ArrayList<>();
        if (food.breakfasts != null) {
            meals.addAll(food.breakfasts);
        }
        if (food.lunches != null) {
            meals.addAll(food.lunches);
        }
        if (food.dinners != null) {
            meals.addAll(food.dinners);
        }

        if (meals.size() != 0) {
            //sorting
            SortUtils.sortMeals(meals);

            tvTotalAmount.setText(getContext().getString(R.string.cart_total_amount, meals.get(0).amount));
            totalAmount = meals.get(0).amount;
            currentSelectedMeal = meals.get(0);

            MealTimeAdapter adapter = new MealTimeAdapter();
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                    LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            adapter.setList(meals);

            adapter.setOnItemClickListener(new MealTimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(MealTimeAdapter.ViewHolder viewHolder, Meal meal,
                                          int position, boolean isSelected) {
                    currentSelectedMeal = meal;
                    tvTotalAmount.setText(getContext().getString(R.string.cart_total_amount, meal.amount));
                    amount = 1;
                    totalAmount = meal.amount;
                    edtAmount.setText(String.valueOf(amount));
                }
            });
        } else {
            tvTotalAmount.setText(getContext().getString(R.string.cart_total_amount, 0));
            edtAmount.setText(String.valueOf(0));
            amount = 0;
        }

    }

    private void addTextChangeListener() {
        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    try {
                        amount = Integer.parseInt(editable.toString());
                        if (amount > totalAmount) {
                            amount = totalAmount;
                            edtAmount.setText(String.valueOf(amount));
                            edtAmount.setSelection(edtAmount.getText().length());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    amount = 0;
                }
            }
        });
    }

    public interface OnClickListener {
        void onClickedAddToCart(int amount, Meal meal);
    }
}
