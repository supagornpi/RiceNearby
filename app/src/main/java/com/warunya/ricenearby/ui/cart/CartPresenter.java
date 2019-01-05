package com.warunya.ricenearby.ui.cart;

import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.model.Cart;

import java.util.List;

public class CartPresenter implements CartContract.Presenter {

    private CartContract.View view;

    CartPresenter(CartContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        CartManager.getUserCarts(new CartManager.QueryListener() {
            @Override
            public void onComplete(List<Cart> carts) {
                view.hideProgress();
                if (carts != null) {
                    view.hideNotFound();
                    view.fetchCart(carts);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void removeCart(String key) {
        CartManager.removeCart(key);
    }
}
