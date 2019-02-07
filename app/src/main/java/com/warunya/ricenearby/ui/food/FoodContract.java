package com.warunya.ricenearby.ui.food;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.User;

import java.util.List;

public interface FoodContract {

    interface Presenter extends BasePresenter {
        void addToCart(Food food, int amount, Meal meal);

        void buyNow(Food food, int amount, Meal meal);

        void getSellerInfo();

        void findRelateFood();

        void checkFollow();

        void follow();

        void unFollow();

    }

    interface View extends BaseView {
        void addCartSuccess();

        void fetchSellerInfo(User user);

        void fetchRelateFood(List<Food> foods);

        void updateFallow(boolean hasFollow);

        void showProgressFollow();

        void hideProgressFollow();
    }
}
