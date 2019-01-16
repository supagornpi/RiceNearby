package com.warunya.ricenearby.ui.food;

import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;

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
    public void addToCart(Food food, int amount) {
        Cart cart = new Cart(UserManager.getUid(), food, amount);
        CartManager.addToCart(cart, amount);
    }
}
