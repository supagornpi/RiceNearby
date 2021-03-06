package com.warunya.ricenearby.ui.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.SimplePagerAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.dialog.AddCartDialog;
import com.warunya.ricenearby.dialog.ImageBitmapDialog;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.cart.CartActivity;
import com.warunya.ricenearby.ui.seller.profile.SellerProfileActivity;
import com.warunya.ricenearby.utils.BitmapUtils;
import com.warunya.ricenearby.utils.ConvertDateUtils;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;
import com.warunya.ricenearby.utils.SortUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class FoodActivity extends AbstractActivity implements FoodContract.View {

    private boolean hasFollow = false;
    private Food food;
    private CustomAdapter<Food> adapter;

    private TextView tvFoodName;
    private TextView tvPrice;
    private TextView tvDetail;
    private TextView tvMealTime;
    private TextView tvSellerName;
    private Button btnFollow;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView btnAddCart;
    private TextView btnBuy;
    private LinearLayout layoutBtnBuy;
    private ImageView ivSellerProfile;
    private RecyclerViewProgress recyclerViewRelate;
    private ProgressBar progressBarFollow;

    private FoodContract.Presenter presenter;

    public static void start(Food food) {
        Intent intent = new Intent(MyApplication.applicationContext, FoodActivity.class);
        intent.putExtra("food", Parcels.wrap(food));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_food;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        showBackButton();

        food = Parcels.unwrap(getIntent().getParcelableExtra("food"));

        bindView();
        bindAction();

        //cannot buy food of self
        layoutBtnBuy.setVisibility(food.uid.equals(UserManager.getUid()) ? View.GONE : View.VISIBLE);

        SimplePagerAdapter<Upload> adapter = new SimplePagerAdapter<Upload>().setOnInflateViewListener(new SimplePagerAdapter.OnInflateViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int position) {
                ImageView ivFood = itemView.findViewById(R.id.iv_food);
//                set banner height
                ivFood.getLayoutParams().height = ResolutionUtils.getBannerHeightFromRatio(itemView.getContext());

                if (((Upload) item) == null) return;
                GlideLoader.load(((Upload) item).url, ivFood);
            }

            @Override
            public int getLayout() {
                return R.layout.item_food_image;
            }
        });

        adapter.setListItems(food.uploads);

        //set product image list
        viewPager.setAdapter(adapter);
        circleIndicator.removeAllViews();
        if (adapter.getCount() > 1) {
            circleIndicator.setViewPager(viewPager);
        }
    }

    private void bindView() {
        tvFoodName = findViewById(R.id.tv_food_name);
        tvPrice = findViewById(R.id.tv_price);
        tvDetail = findViewById(R.id.tv_detail);
        tvMealTime = findViewById(R.id.tv_meal_time);
        tvSellerName = findViewById(R.id.tv_seller_name);
        btnFollow = findViewById(R.id.btn_follow);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleindicator);
        btnAddCart = findViewById(R.id.btn_add_cart);
        btnBuy = findViewById(R.id.btn_buy);
        layoutBtnBuy = findViewById(R.id.layout_buy);
        ivSellerProfile = findViewById(R.id.iv_seller_profile);
        recyclerViewRelate = findViewById(R.id.recyclerView_relate);
        progressBarFollow = findViewById(R.id.progress_follow);

        initRecyclerView();

        if (food == null) return;
        presenter = new FoodPresenter(this, food.uid);

        tvFoodName.setText(food.foodName);
        tvPrice.setText(food.price + ".-");
        tvDetail.setText(food.detail);

        //footer
        presenter.getSellerInfo();
        presenter.findRelateFood();

        if (food.uid.equals(UserManager.getUid())) {
            //can not follow your self
            btnFollow.setVisibility(View.GONE);
        } else {
            presenter.checkFollow();
        }

        //Meal time
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
            StringBuilder allMeal = new StringBuilder();
            for (Meal meal : meals) {
                allMeal.append(ConvertDateUtils.getNewDateFormatFOrMealTime(meal.date))
                        .append(" ")
                        .append(meal.mealTime.getMealTimeText())
                        .append("     ");
            }
            tvMealTime.setText(allMeal.toString());
        }

    }

    private void bindAction() {
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (food == null) return;
                AddCartDialog.show(FoodActivity.this, food, new AddCartDialog.OnClickListener() {
                    @Override
                    public void onClickedAddToCart(int amount, Meal meal) {
                        presenter.addToCart(food, amount, meal);
                        //play animate add to cart
                        Bitmap bitmap = BitmapUtils.getBitmapFromView(viewPager);
                        ImageBitmapDialog imageBitmapDialog = new ImageBitmapDialog(FoodActivity.this, bitmap);
                        imageBitmapDialog.show();
                    }
                });
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food == null) return;
                AddCartDialog.showBuyNow(FoodActivity.this, food, new AddCartDialog.OnClickListener() {
                    @Override
                    public void onClickedAddToCart(int amount, Meal meal) {
                        presenter.buyNow(food, amount, meal);
                    }
                });
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasFollow) {
                    hasFollow = false;
                    presenter.unFollow();
                } else {
                    hasFollow = true;
                    presenter.follow();
                }
                btnFollow.setText(hasFollow ? "ยกเลิกติดตาม" : "ติดตาม");
            }
        });

        tvSellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerProfileActivity.start(food.uid);
            }
        });

        ivSellerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerProfileActivity.start(food.uid);
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewRelate.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodGridView) itemView).setHorizontalItem();
                ((FoodGridView) itemView).bind(((Food) item));
                ((FoodGridView) itemView).bindAction();

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodGridView(parent.getContext());
            }
        });
        recyclerViewRelate.recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showNotFound() {

    }

    @Override
    public void hideNotFound() {

    }

    @Override
    public void addCartSuccess() {
        CartActivity.start();
    }

    @Override
    public void fetchSellerInfo(User user) {
        setTitle(user.name == null ? user.username : user.name);
        tvSellerName.setText(user.name == null ? user.username : user.name);
        if (user.image != null) {
            GlideLoader.loadImageCircle(user.image.url, ivSellerProfile);
        }
    }

    @Override
    public void fetchRelateFood(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void updateFallow(boolean hasFollow) {
        this.hasFollow = hasFollow;
        btnFollow.setText(hasFollow ? "ยกเลิกติดตาม" : "ติดตาม");
    }

    @Override
    public void showProgressFollow() {
        progressBarFollow.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressFollow() {
        progressBarFollow.setVisibility(View.GONE);
    }

}
