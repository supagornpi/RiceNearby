package com.warunya.ricenearby.ui.cart;

public class CartPresenter implements CartContract.Presenter {

    private CartContract.View view;

    CartPresenter(CartContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
