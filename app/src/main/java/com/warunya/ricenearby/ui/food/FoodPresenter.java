package com.warunya.ricenearby.ui.food;

import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;

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

        CartManager.addToCart(cart, meal, amount);
    }
}
