package com.warunya.ricenearby.ui.food;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class FoodPresenter implements FoodContract.Presenter {

    private FoodContract.View view;

    FoodPresenter(FoodContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void addToCart(Food food, int amount, Meal meal) {
        Cart cart = new Cart(UserManager.getUid(), food, amount);
        CartManager.addToCart(cart, meal, amount, null);
    }

    @Override
    public void buyNow(Food food, int amount, Meal meal) {
        view.showProgress();
        Cart cart = new Cart(UserManager.getUid(), food, amount);
        CartManager.addToCart(cart, meal, amount, new CartManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgress();
                view.addCartSuccess();
            }
        });
    }

    @Override
    public void getSellerInfo(String uid) {
        UserManager.getUserProfile(uid, new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    view.fetchSellerInfo(user);
                }
            }
        });

    }

    @Override
    public void findRelateFood(String uid) {
        FoodManager.getUserFoods(uid, new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.fetchRelateFood(foods);
            }
        });
    }
}
