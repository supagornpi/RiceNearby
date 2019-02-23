package com.warunya.ricenearby.ui.home;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.constant.FoodType;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.SimplePagerAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.FoodTypeView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Promotion;
import com.warunya.ricenearby.ui.cart.CartActivity;
import com.warunya.ricenearby.ui.home.viewall.ViewAllFoodActivity;
import com.warunya.ricenearby.ui.search.SearchActivity;
import com.warunya.ricenearby.ui.web.WebViewActivity;
import com.warunya.ricenearby.utils.ResolutionUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends AbstractFragment implements HomeContract.View {

    private HomeContract.Presenter presenter = new HomePresenter(this);
    private CustomAdapter<Food> adapterRecommend;
    private CustomAdapter<Food> adapterFollow;
    private CustomAdapter<Food> adapterNearby;
    private CustomAdapter<FoodType> adapterFoodType;

    private RecyclerViewProgress rvpRecommend;
    private RecyclerViewProgress rvpFollow;
    private RecyclerViewProgress rvpNearby;
    private RecyclerViewProgress rvpFoodType;
    private ViewPager viewPagerPromotion;
    private CircleIndicator circleIndicator;
    private TextView edtSearch;
    private ImageView ivCart;
    private TextView tvSeeAllRecommend;
    private TextView tvSeeAllFollow;
    private TextView tvSeeAllNearby;

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupView(@NotNull View view) {
        bindView(view);
        bindAction();
        initSearchView();

        presenter.start();
        presenter.getFollow();
        presenter.getNearBy();
    }

    private void bindView(View view) {
        rvpRecommend = view.findViewById(R.id.recyclerViewProgress);
        rvpFollow = view.findViewById(R.id.recyclerViewProgress_follow);
        rvpNearby = view.findViewById(R.id.recyclerViewProgress_nearby);
        rvpFoodType = view.findViewById(R.id.recyclerViewProgress_food_type);
        viewPagerPromotion = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.circleindicator);
        tvSeeAllRecommend = view.findViewById(R.id.tv_see_all_recommend);
        tvSeeAllFollow = view.findViewById(R.id.tv_see_all_follow);
        tvSeeAllNearby = view.findViewById(R.id.tv_see_all_nearby);

        edtSearch = view.findViewById(R.id.edt_search);
        ivCart = view.findViewById(R.id.iv_cart);

        adapterRecommend = initRecyclerViewFood(rvpRecommend);
        adapterFollow = initRecyclerViewFood(rvpFollow);
        adapterNearby = initRecyclerViewFood(rvpNearby);
        initRecyclerViewFoodType();

        initViewPagerPromotion();
    }

    private void bindAction() {
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.start();
            }
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.start();
            }
        });

        tvSeeAllRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Food> foods = presenter.getRecommendFoods();
                if (foods != null && foods.size() != 0) {
                    ViewAllFoodActivity.start("อาหารแนะนำ", foods);
                }
            }
        });
        tvSeeAllFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Food> foods = presenter.getFollowFoods();
                if (foods != null && foods.size() != 0) {
                    ViewAllFoodActivity.start("ร้านที่คุณติดตาม", foods);
                }
            }
        });
        tvSeeAllNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Food> foods = presenter.getNearbyFoods();
                if (foods != null && foods.size() != 0) {
                    ViewAllFoodActivity.start("ร้านใกล้ตัว", foods);
                }
            }
        });
    }

    private CustomAdapter<Food> initRecyclerViewFood(RecyclerViewProgress rvp) {
        rvp.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        CustomAdapter<Food> customAdapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodGridView) itemView).bind(((Food) item));
                ((FoodGridView) itemView).bindAction();

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodGridView(parent.getContext());
            }
        });

        customAdapter.setMaximumItem(10);
        rvp.recyclerView.setAdapter(customAdapter);
        return customAdapter;
    }

    private void initRecyclerViewFoodType() {
        rvpFoodType.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterFoodType = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodTypeView) itemView).bind(((FoodType) item), position);
                ((FoodTypeView) itemView).bindAction(((FoodType) item));
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodTypeView(parent.getContext());
            }
        });

        rvpFoodType.recyclerView.setAdapter(adapterFoodType);

        List<FoodType> foodTypes = new ArrayList<>();
        foodTypes.add(FoodType.ChineseFood);
        foodTypes.add(FoodType.HealthyFood);
        foodTypes.add(FoodType.HalalFood);
        foodTypes.add(FoodType.KoreanFood);
        foodTypes.add(FoodType.VegetarianFood);
        foodTypes.add(FoodType.JapaneseFood);
        foodTypes.add(FoodType.ThaiFood);
        adapterFoodType.setListItem(foodTypes);
    }

    private void initViewPagerPromotion() {
        SimplePagerAdapter<Promotion> adapter = new SimplePagerAdapter<Promotion>().setOnInflateViewListener(new SimplePagerAdapter.OnInflateViewListener() {
            @Override
            public <T> void onBindViewHolder(final T item, View itemView, int position) {
                ImageView ivFood = itemView.findViewById(R.id.iv_food);
//                set banner height
                ivFood.getLayoutParams().height = ResolutionUtils.getBannerPromotionHeightFromRatio(itemView.getContext());

                ivFood.setImageResource(((Promotion) item).imageId);

                ivFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WebViewActivity.start(((Promotion) item).url);
                    }
                });
            }

            @Override
            public int getLayout() {
                return R.layout.item_food_image;
            }
        });

        adapter.setListItems(getPromotion());

        //set product image list
        viewPagerPromotion.setAdapter(adapter);
        circleIndicator.removeAllViews();
        if (adapter.getCount() > 1) {
            circleIndicator.setViewPager(viewPagerPromotion);
        }
    }

    private void initSearchView() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    presenter.filterFoods(charSequence.toString());
                } else {
                    presenter.start();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void fetchFoods(List<Food> foods) {
        adapterRecommend.setListItem(foods);
        tvSeeAllRecommend.setVisibility(foods.size() > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void fetchFollow(List<Food> foods) {
        adapterFollow.addListItem(foods);
        tvSeeAllFollow.setVisibility(foods.size() > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void fetchNearby(List<Food> foods) {
        adapterNearby.setListItem(foods);
        tvSeeAllNearby.setVisibility(foods.size() > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showProgressRecommend() {
        rvpRecommend.showProgress();
    }

    @Override
    public void hideProgressRecommend() {
        rvpRecommend.hideProgress();
    }

    @Override
    public void showProgressFollow() {
        rvpFollow.showProgress();
    }

    @Override
    public void hideProgressFollow() {
        rvpFollow.hideProgress();
    }

    @Override
    public void notFoundFollow() {
        rvpFollow.setTextNotFound("คุณยังไม่ได้ติดตามร้านค้า");
        rvpFollow.showNotFound();
    }

    @Override
    public void showProgressNearby() {
        rvpNearby.showProgress();
    }

    @Override
    public void hideProgressNearby() {
        rvpNearby.hideProgress();

    }

    @Override
    public void showProgress() {
        rvpRecommend.showProgress();
    }

    @Override
    public void hideProgress() {
        rvpRecommend.hideProgress();
    }

    @Override
    public void showNotFound() {
        rvpRecommend.showNotFound();
    }

    @Override
    public void hideNotFound() {
        rvpRecommend.hideNotFound();
    }

    private List<Promotion> getPromotion() {
        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new Promotion(R.drawable.mk, "https://www.mkrestaurant.com/th/delivery"));
        promotions.add(new Promotion(R.drawable.burgerking, "https://delivery.burgerking.co.th"));
        promotions.add(new Promotion(R.drawable.kfc, "https://www.kfc.co.th/menu#/"));
        promotions.add(new Promotion(R.drawable.yayoi, "https://www.yayoirestaurants.com/th"));
        promotions.add(new Promotion(R.drawable.oishi, "https://oishidelivery.com/th"));
        promotions.add(new Promotion(R.drawable.pizza, "https://www.1112.com/"));
        return promotions;
    }
}
